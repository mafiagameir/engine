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
import co.mafiagame.common.domain.Action;
import co.mafiagame.common.domain.Audit;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.Command;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.command.internalcommand.context.StartGameCommandContext;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.container.StashedGameContainer;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameState;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.domain.Role;
import co.mafiagame.engine.executor.CommandExecutor;
import co.mafiagame.persistence.api.PersistenceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author nazila
 */
@Component
public class StartGameCommand implements Command<StartGameCommandContext> {

    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private PersistenceApi persistenceApi;
    @Autowired
    private StashedGameContainer stashedGameContainer;

    @Override
    public ResultMessage execute(StartGameCommandContext context) throws Exception {
        Game game = new Game(context.getStashedGame());
        assignRoles(game.getPlayers(), game.getGameState());
        game.assignBackupPlayers();
        gameContainer.addGame(game);
        stashedGameContainer.removeGame(context.getStashedGame());
        game.getPlayers()
                .stream()
                .forEach(
                        p -> persistenceApi.saveAudit(
                                new Audit(p.getAccount(), Action.START_GAME, context.getInterfaceContext().getRoomId())
                        )
                );

        commandExecutor.run(context.getInterfaceContext(),
                Constants.CMD.Internal.ANNOUNCE_ROLES,
                new EmptyContext(context.getInterfaceContext(), game));
        return new ResultMessage(new Message("game.started"),
                ChannelType.GENERAL, context.getInterfaceContext());
    }

    private void assignRoles(List<Player> players, GameState gameState) {
        Collections.shuffle(players);
        int assigned = 0;
        for (int i = 0; i < gameState.getCitizenNum(); i++)
            players.get(assigned + i).setRole(Role.CITIZEN);
        assigned += gameState.getCitizenNum();
        for (int i = 0; i < gameState.getMafiaNum(); i++)
            players.get(assigned + i).setRole(Role.MAFIA);
        assigned += gameState.getMafiaNum();
        for (int i = 0; i < gameState.getDetectiveNum(); i++)
            players.get(assigned + i).setRole(Role.DETECTIVE);
        assigned += gameState.getDetectiveNum();
        for (int i = 0; i < gameState.getDoctorNum(); i++)
            players.get(assigned + i).setRole(Role.DOCTOR);
        Collections.shuffle(players);
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.START_GAME;
    }

}
