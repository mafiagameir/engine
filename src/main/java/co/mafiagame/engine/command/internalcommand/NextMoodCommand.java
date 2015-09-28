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

package co.mafiagame.engine.command.internalcommand;

import co.mafiagame.common.Constants;
import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.Command;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.domain.GameResult;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hekmatof
 */
@Component
public class NextMoodCommand implements Command<EmptyContext> {

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(EmptyContext context) {
        Game game = context.getGame();
        if (game.checkGameOver() != GameResult.UNKNOWN)
            commandExecutor.run(context.getInterfaceContext(),
                    Constants.CMD.Internal.HANDLE_GAME_OVER, new EmptyContext(
                            context.getInterfaceContext(), game));
        else {
            GameMood gameMood = game.nextMode();
            if (gameMood == GameMood.DAY)
                return nextModeIsDay(context.getInterfaceContext(), game);
            if (gameMood == GameMood.NIGHT_MAFIA)
                return nextModeIsMafiaNight(context.getInterfaceContext(), game);
            if (gameMood == GameMood.NIGHT_DETECTOR)
                return nextModeIsDetectorNight(context.getInterfaceContext(), game);
            if (gameMood == GameMood.NIGHT_DOCTOR)
                return nextModeIsDoctorNight(context.getInterfaceContext(), game);
            game.setGameMood(game.nextMode());
        }
        return new ResultMessage(new Message("", null, null), ChannelType.NONE,
                context.getInterfaceContext());
    }

    private ResultMessage nextModeIsDoctorNight(InterfaceContext interfaceContext, Game game) {
        game.setGameMood(game.nextMode());
        Account doctorAccount = game.getDoctor().getAccount();
        return new ResultMessage(
                new Message("doctor.night.started",
                        doctorAccount.getUserInterfaceId(),
                        doctorAccount.getUsername(),
                        game.makeOption(Constants.CMD.DOCTOR_HEAL, false),
                        Collections.singletonList(doctorAccount.getUsername())
                ),
                ChannelType.USER_PRIVATE, interfaceContext);
    }

    private ResultMessage nextModeIsDetectorNight(InterfaceContext interfaceContext, Game game) {
        game.setGameMood(game.nextMode());
        Account detectorAccount = game.getDetector().getAccount();
        return new ResultMessage(
                new Message("detector.night.started", detectorAccount.getUserInterfaceId(),
                        detectorAccount.getUsername(),
                        game.makeOption(Constants.CMD.DETECTOR_ASK, false),
                        Collections.singletonList(detectorAccount.getUsername())
                ),
                ChannelType.USER_PRIVATE, interfaceContext
        );
    }

    private ResultMessage nextModeIsMafiaNight(InterfaceContext interfaceContext, Game game) {
        List<Message> messageList = game.getMafias().stream()
                .map(Player::getAccount)
                .map(a -> new Message("mafia.night.started", a.getUserInterfaceId(),
                                a.getUsername(),
                                game.makeOption(Constants.CMD.MAFIA_VOTE, true),
                                Collections.singletonList(a.getUsername())
                        )
                )
                .collect(Collectors.toList());
        game.setGameMood(game.nextMode());
        return new ResultMessage(messageList, ChannelType.USER_PRIVATE, interfaceContext);
    }

    private ResultMessage nextModeIsDay(InterfaceContext context, Game game) {
        List<Message> messages = new ArrayList<>();
        if (game.isTellGameState())
            messages.add(new Message("game.state.is", null, String.valueOf(game
                    .getGameState().getMafiaNum()), String.valueOf(game
                    .getGameState().getCitizenNum())));
        game.toggleTellGameState();
        Player tempKilled = game.getKillCandidate();
        if (game.getHealCandidate() == null) {
            game.killPlayer(tempKilled);
            game.clearKillCandidate();
        } else if (game.getHealCandidate().equals(tempKilled)) {
            game.clearKillCandidate();
            game.setGameMood(game.nextMode());
            messages.add(new Message("nobody.was.killed.last.night", null, null));
            return new ResultMessage(messages, ChannelType.GENERAL, context);
        } else {
            game.killPlayer(tempKilled);
            game.clearKillCandidate();
        }

        if (game.checkGameOver() != GameResult.UNKNOWN)
            commandExecutor.run(context,
                    Constants.CMD.Internal.HANDLE_GAME_OVER, new EmptyContext(context, game));
        game.setGameMood(game.nextMode());
        if (tempKilled == null)
            messages.add(new Message("nobody.was.killed.last.night", null, null));
        else
            messages.add(new Message("user.was.killed.last.night", null,
                    tempKilled.getAccount().getUsername()));
        return new ResultMessage(messages, ChannelType.GENERAL, context);
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.NEXT_MOOD;
    }
}
