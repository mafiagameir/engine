/*
 * Copyright (C) 2015 mafiagame.ir
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.engine.api;

import co.mafiagame.common.Constants;
import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.engine.command.context.*;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * main engine API for use in interfaces
 *
 * @author hekmatof
 * @author nazila
 */

@Component
public class GameApi {

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private GameContainer gameContainer;

    public void startStashedGame(InterfaceContext interfaceContext,
                                 int citizenNum, int mafiaNum, int detectiveNum, int doctorNum) {
        commandExecutor.run(interfaceContext, Constants.CMD.START_STASHED_GAME,
                new StartStashedGameCommandContext(interfaceContext,
                        citizenNum, mafiaNum, detectiveNum, doctorNum));
    }

    public void register(InterfaceContext interfaceContext, String username, String firstName, String lastName) {
        commandExecutor.run(interfaceContext, Constants.CMD.REGISTER,
                new RegisterCommandContext(interfaceContext, username, firstName, lastName));
    }

    public void startElection(InterfaceContext interfaceContext) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.START_ELECTION,
                new EmptyContext(interfaceContext, game));
    }

    public void vote(InterfaceContext interfaceContext, String voterUsername,
                     List<String> votedUsername) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.VOTE,
                new VoteCommandContext(interfaceContext, game, voterUsername,
                        votedUsername));
    }

    public void detectiveAsk(InterfaceContext interfaceContext, String username,
                            String who) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.DETECTIVE_ASK,
                new DetectiveAskCommandContext(interfaceContext, game, username,
                        who));
    }

    public void startFinalElection(InterfaceContext interfaceContext) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext,
                Constants.CMD.START_FINAL_ELECTION, new EmptyContext(interfaceContext, game));
    }

    public void mafiaKillVote(InterfaceContext interfaceContext,
                              String voterMafia, String username) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.MAFIA_VOTE,
                new MafiaKillsCommandContext(interfaceContext, game, voterMafia, username));
    }

    public void doctorHeal(InterfaceContext interfaceContext,
                           String doctorName, String healedName) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.DOCTOR_HEAL,
                new DoctorHealCommandContext(interfaceContext, game, doctorName, healedName));
    }

    public void whoIsPlaying(InterfaceContext interfaceContext) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.WHO_IS_PLAYING,
                new EmptyContext(interfaceContext, game));
    }

    public void help(InterfaceContext interfaceContext) {
        commandExecutor.run(interfaceContext, Constants.CMD.HELP, new EmptyContext(interfaceContext, null));
    }

    public void cancelGame(InterfaceContext interfaceContext, String username) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.CANCEL,
                new CancelCommandContext(interfaceContext, game, username));
    }

    public void commandNotFound(InterfaceContext interfaceContext) {
        commandExecutor.run(interfaceContext, Constants.CMD.COMMAND_NOT_FOUND, new EmptyContext(interfaceContext, null));
    }

    public void whatIsMyRole(InterfaceContext interfaceContext) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.WHAT_IS_MY_ROLE,
                new EmptyContext(interfaceContext, game));
    }

    public void killMe(InterfaceContext interfaceContext) {
        Game game = gameContainer.getGame(interfaceContext);
        commandExecutor.run(interfaceContext, Constants.CMD.KILL_ME,
                new EmptyContext(interfaceContext, game));
    }

}
