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
import co.mafiagame.engine.command.context.CommandContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.exception.UsernameNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author nazila
 */
abstract class VotableCommand<T extends CommandContext> implements Command<T> {

    protected void vote(Player voter, List<String> votedUserNames, Game game) {
        if (votedUserNames == null || votedUserNames.size() == 0)
            throw new UsernameNotNull();
        Map<Player, List<Player>> playerVote = game.getPlayerVote();
        if (voter.isVoted()) {
            playerVote.keySet().stream()
                    .filter(p -> playerVote.get(p).contains(voter))
                    .forEach(p -> playerVote.get(p).remove(voter));
        }
        for (String votedUsername : votedUserNames) {
            if (votedUsername.equals(Constants.NO_BODY))
                continue;
            Player voted = game.playerByUsername(votedUsername);
            if (playerVote.containsKey(voted))
                playerVote.get(voted).add(voter);
            else {
                List<Player> voters = new ArrayList<>();
                voters.add(voter);
                playerVote.put(voted, voters);
            }
        }
    }
}
