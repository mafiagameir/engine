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

package co.mafiagame.engine.command.internalcommand;

import co.mafiagame.common.Constants;
import co.mafiagame.common.channel.InterfaceChannel;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.Command;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class PurgeCommand implements Command<EmptyContext> {
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private InterfaceChannel interfaceChannel;

    @Override
    public ResultMessage execute(EmptyContext context) throws Exception {
        Game game = context.getGame();
        gameContainer.finished(game.getInterfaceContext());
        interfaceChannel.gameOver(game.getBackupPlayerState().keySet());
        return new ResultMessage(new Message("game.purged"), ChannelType.GENERAL,
                game.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.PURGE;
    }
}
