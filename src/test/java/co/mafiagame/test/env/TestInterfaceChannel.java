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

package co.mafiagame.test.env;

import co.mafiagame.common.channel.InterfaceChannel;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.common.utils.MessageHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author hekmatof
 */
@Component
public class TestInterfaceChannel implements InterfaceChannel {
    private final List<ResultMessage> msgs = new ArrayList<>();

    private boolean print;

    public void setPrint(boolean print) {
        this.print = print;
    }

    @Override
    public void send(ResultMessage resultMessage) {
        synchronized (msgs) {
            msgs.add(resultMessage);
            if (print)
                if (resultMessage.getMessages() != null)
                    resultMessage.getMessages().forEach(
                            msg -> System.out.println(resultMessage
                                    .getChannelType()
                                    + " "
                                    + msg.getReceiverId()
                                    + " :"
                                    + MessageHolder.get(msg.getMessageCode(),
                                    MessageHolder.Lang.EN,
                                    msg.getArgs())));
        }
    }

    @Override
    public void gameOver(Set<String> usernames) {

    }

    public void printMessages() {
        synchronized (msgs) {
            msgs.stream().forEach(
                    result -> {
                        if (result.getMessages() != null)
                            result.getMessages().forEach(
                                    msg -> System.out.println(result
                                            .getChannelType()
                                            + " "
                                            + msg.getReceiverId()
                                            + " :"
                                            + MessageHolder.get(
                                            msg.getMessageCode(),
                                            MessageHolder.Lang.EN,
                                            msg.getArgs())));
                    });
        }
    }

    public boolean lastKeyIs(String messageCode) {
        synchronized (msgs) {
            ResultMessage msg = msgs.get(msgs.size() - 1);
            return msg.getMessages().stream().anyMatch(
                    m -> m.getMessageCode().equals(messageCode));
        }
    }

    public boolean containKey(String messageCode) {
        synchronized (msgs) {
            return msgs.stream().anyMatch(
                    r -> r.getMessages().stream().anyMatch(
                            m -> m.getMessageCode().equals(messageCode)));
        }
    }
}
