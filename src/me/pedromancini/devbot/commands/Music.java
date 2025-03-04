package me.pedromancini.devbot.commands;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import me.pedromancini.devbot.main.DevBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;
import java.util.Objects;

public class Music extends ListenerAdapter {

    private final AudioPlayerManager playerManager;

    public Music() {
        // Inicializa o AudioPlayerManager
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager); // Para fontes remotas como YouTube
        AudioSourceManagers.registerLocalSource(playerManager);  // Para arquivos locais, se necessário
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);

        // Recupera o prefixo definido no servidor
        String prefix = String.valueOf(DevBot.prefixMap.get(event.getGuild().getId()));

        // Divide a mensagem em partes
        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();

        // Verifica se a mensagem começa com o prefixo e o comando "play"
        if (args[0].equalsIgnoreCase(prefix + "play")) {
            if (args.length < 2) {
                textChannel.sendMessage("Por favor, forneça o nome ou URL da música para tocar.").queue();
                return;
            }

            String query = String.join(" ", args).substring(prefix.length() + 5); // Remover "play" do comando
            playMusic(event.getGuild(), query, textChannel);
        }
    }

    private void playMusic(Guild guild, String query, TextChannel textChannel) {
        // Implementa a lógica para procurar a música com Lavaplayer
        searchForTrack(query, textChannel);
    }

    private void searchForTrack(String query, TextChannel textChannel) {
        playerManager.loadItem(query, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                // Caso a música seja encontrada, toca ela
                textChannel.sendMessage("Tocando agora: " + track.getInfo().title).queue();
                playInVoiceChannel(track, (VoiceChannel) Objects.requireNonNull(textChannel.getGuild().getSelfMember().getVoiceState()).getChannel());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // Caso a playlist seja encontrada, pega o primeiro item e toca
                AudioTrack track = playlist.getSelectedTrack();
                if (track == null) {
                    track = playlist.getTracks().get(0);
                }
                textChannel.sendMessage("Tocando agora: " + track.getInfo().title).queue();
                playInVoiceChannel(track, (VoiceChannel) Objects.requireNonNull(textChannel.getGuild().getSelfMember().getVoiceState()).getChannel());
            }

            @Override
            public void noMatches() {
                textChannel.sendMessage("Não consegui encontrar a música. Tente novamente!").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                textChannel.sendMessage("Erro ao carregar a música: " + exception.getMessage()).queue();
            }
        });
    }

    private void playInVoiceChannel(AudioTrack track, VoiceChannel voiceChannel) {
        if (voiceChannel == null) {
            return;
        }

        // Verifica se o bot tem permissão para falar no canal de voz
        if (!voiceChannel.getGuild().getSelfMember().hasPermission(voiceChannel, net.dv8tion.jda.api.Permission.VOICE_CONNECT, net.dv8tion.jda.api.Permission.VOICE_SPEAK)) {
            return;
        }

        // Aqui você pode adicionar código para conectar ao canal de voz, se necessário
        AudioPlayer player = playerManager.createPlayer();
        AudioPlayerSendHandler audioSendHandler = new AudioPlayerSendHandler(player);

        // Conecta ao canal de voz
        voiceChannel.getGuild().getAudioManager().openAudioConnection(voiceChannel);

        // Play the track
        player.playTrack(track);

        // Define o handler para enviar o áudio
        voiceChannel.getGuild().getAudioManager().setSendingHandler(audioSendHandler);
    }

    // Classe do AudioPlayerSendHandler que envia o áudio
    public static class AudioPlayerSendHandler implements AudioSendHandler {
        private final AudioPlayer audioPlayer;
        private AudioFrame lastFrame;

        public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
            this.audioPlayer = audioPlayer;
        }

        @Override
        public boolean canProvide() {
            lastFrame = audioPlayer.provide();
            return lastFrame != null;
        }

        @Override
        public ByteBuffer provide20MsAudio() {
            return ByteBuffer.wrap(lastFrame.getData());
        }

        @Override
        public boolean isOpus() {
            return true;
        }
    }
}
