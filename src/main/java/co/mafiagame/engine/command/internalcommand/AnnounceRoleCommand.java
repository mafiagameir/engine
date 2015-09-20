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
import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.common.utils.ListToString;
import co.mafiagame.engine.command.Command;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nazila
 */
@Component
public class AnnounceRoleCommand implements Command<EmptyContext> {

    @Override
    public ResultMessage execute(EmptyContext context) {
        Game game = context.getGame();
        List<Message> messages = new ArrayList<>();
        game.getPlayers()
                .stream()
                .forEach(
                        p -> {
                            switch (p.getRole()) {
                                case CITIZEN:
                                    messages.add(new Message("your.role.is.citizen",
                                            p.getAccount().getUserInterfaceId()));
                                    break;
                                case MAFIA:
                                    messages.add(new Message("your.role.is.mafia",
                                            p.getAccount().getUserInterfaceId()));
                                    break;
                                case DETECTOR:
                                    messages.add(new Message("your.role.is.detector",
                                            p.getAccount().getUserInterfaceId()));
                                    break;
                                case DOCTOR:
                                    messages.add(new Message("your.role.is.doctor",
                                            p.getAccount().getUserInterfaceId()));
                                    break;
                            }
                        });
        List<Player> mafias = game.getMafias();
        List<String> mafiaUserNames = mafias.stream().map(Player::getAccount)
                .map(Account::getUsername).collect(Collectors.toList());
        mafias.stream().forEach(
                m -> messages.add(new Message("mafia.are.players",
                        m.getAccount().getUserInterfaceId(),
                        ListToString.toString(mafiaUserNames))));
        return new ResultMessage(messages, ChannelType.USER_PRIVATE, context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.ANNOUNCE_ROLES;
    }

}
