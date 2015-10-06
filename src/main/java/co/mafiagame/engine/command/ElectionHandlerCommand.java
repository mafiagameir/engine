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

import co.mafiagame.common.domain.result.Message;
import co.mafiagame.engine.command.context.CommandContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.Player;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author nazila
 */
public abstract class ElectionHandlerCommand<T extends CommandContext> implements Command<T> {
    protected List<Player> findMaxVoted(Game game, boolean isMafiaVote) {
        Map<Player, List<Player>> votes = game.getPlayerVote();
        long noBodyCount;
        if (isMafiaVote) {
            noBodyCount = game.getMafias().stream().filter(p -> {
                for (Player key : votes.keySet())
                    if (votes.get(key).contains(p))
                        return false;
                return true;
            }).count();
        } else {
            noBodyCount = game.getPlayers().stream().filter(p -> {
                for (Player key : votes.keySet())
                    if (votes.get(key).contains(p))
                        return false;
                return true;
            }).count();
        }
        Player maxVoted;
        try {
            maxVoted = votes.keySet().stream()
                    .max((p1, p2) -> votes.get(p1).size() - votes.get(p2).size()).get();
        } catch (NoSuchElementException e) {
            return null;
        }
        int maxVoteNum = votes.get(maxVoted).size();
        if (noBodyCount > maxVoteNum)
            return null;
        return votes.keySet().stream()
                .filter(p -> votes.get(p).size() == maxVoteNum)
                .collect(Collectors.toList());
    }

    protected void electionResult(List<Message> messages, Game game) {
        Map<Player, List<Player>> votes = game.getPlayerVote();
        votes.keySet().stream().forEach(
                p -> messages.add(new Message("vote.for.user", null,null,
                        p.getAccount().getUsername(),
                        String.valueOf(votes.get(p).size())
                ))
        );
    }
}
