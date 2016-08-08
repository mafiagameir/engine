/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.common;

/**
 * @author hekmatof
 */
public class Constants {
    public static final String NO_BODY = "nobody";

    public static class CONF {
        public static final String MESSAGE_FILE = "messages-";
    }

    public static class CMD {
        public static final String REGISTER = "register";
        public static final String START_STASHED_GAME = "start";
        public static final String VOTE = "vote";
        public static final String START_ELECTION = "election";
        public static final String DETECTIVE_ASK = "ask";
        public static final String START_FINAL_ELECTION = "final_election";
        public static final String MAFIA_VOTE = "kill";
        public static final String DOCTOR_HEAL = "heal";
        public static final String WHO_IS_PLAYING = "who_is_playing";
        public static final String HELP = "help";
        public static final String CANCEL = "cancel";
        public static final String COMMAND_NOT_FOUND = "command_not_found";
        public static final String WHAT_IS_MY_ROLE = "what_is_my_role";
        public static final String KILL_ME= "kill_me";
        public static final String LANG= "lang";

        public static class Internal {
            public static final String ELECTION_FINISHED = "electionFinished";
            public static final String START_GAME = "startGame";
            public static final String MAFIA_ELECTION_FINISHED = "mafiaElectionFinished";
            public static final String NEXT_MOOD = "nextMood";
            public static final String HANDLE_GAME_OVER = "handleGameOver";
            public static final String ANNOUNCE_ROLES = "announceRoles";
            public static final String PURGE = "purge";
            public static final String PURGE_ALARM = "purgeAlarm";
        }

    }
}
