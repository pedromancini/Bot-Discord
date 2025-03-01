package me.pedromancini.devbot.commands;

import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberJoin extends ListenerAdapter {

    @Override
    public void onThreadMemberJoin(@NotNull ThreadMemberJoinEvent event) {
        super.onThreadMemberJoin(event);


    }
}
