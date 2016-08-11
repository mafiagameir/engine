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

package co.mafiagame.engine.command;

import co.mafiagame.common.Constants;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.command.context.MafiaKillsCommandContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.domain.Role;
import co.mafiagame.engine.exception.MafiaVoteOnWrongMoodException;
import co.mafiagame.engine.exception.YouAreNotMafiaException;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author nazila
 */
@Component
public class MafiaKillsCommand extends VotableCommand<MafiaKillsCommandContext> {

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(MafiaKillsCommandContext context) {
        validateGameNotNull(context);
        Game game = context.getGame();
        game.update();
        Player voter = game.playerByUsername(context.getMafiaVoter());
        validate(context);
        vote(voter, Collections.singletonList(context.getUserVoted()), game);
        voter.setVoted(true);
        if (game.checkMafiaElectionIsOver())
            commandExecutor.run(context.getInterfaceContext(),
                    Constants.CMD.Internal.MAFIA_ELECTION_FINISHED,
                    new EmptyContext(context.getInterfaceContext(), game));
        List<Message> messages = new ArrayList<>();
        if (Constants.NO_BODY.equals(context.getUserVoted()))
            game.mafias().forEach(m -> messages.add(createResultMessage(context, "user.vote.nobody", m)));
        else
            game.mafias().forEach(m -> messages.add(createResultMessage(context, "user.vote.another", m)));

        return new ResultMessage(
                messages, context.getInterfaceContext().getSenderType(), context.getInterfaceContext());
    }

    private Message createResultMessage(MafiaKillsCommandContext context, String messageCode, Player mafia) {
        return new Message(messageCode)
                .setReceiverId(mafia.getAccount().getUserInterfaceId())
                .setArgs(context.getMafiaVoter());
    }

    private void validate(MafiaKillsCommandContext context) {
        Game game = context.getGame();
        Player voter = game.playerByUsername(context.getMafiaVoter());
        if (game.getGameMood() != GameMood.NIGHT_MAFIA)
            throw new MafiaVoteOnWrongMoodException();
        if (voter.getRole() != Role.MAFIA)
            throw new YouAreNotMafiaException();
    }

    @Override
    public String commandName() {
        return Constants.CMD.MAFIA_VOTE;
    }

}
