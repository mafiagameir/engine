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

package co.mafiagame.common.domain.result;

import co.mafiagame.common.channel.InterfaceContext;

import java.util.Collections;
import java.util.List;

/**
 * @author hekmatof
 */
public class ResultMessage {
    private final List<Message> messages;
    private final ChannelType channelType;
    private final InterfaceContext ic;


    public ResultMessage(List<Message> message, ChannelType channelType, InterfaceContext ic) {
        this.messages = message;
        this.channelType = channelType;
        this.ic = ic;
    }

    public ResultMessage(Message message, ChannelType channelType, InterfaceContext ic) {
        this(Collections.singletonList(message), channelType, ic);
    }


    public List<Message> getMessages() {
        return messages;
    }

    public ChannelType getChannelType() {
        return channelType;
    }


    public InterfaceContext getIc() {
        return ic;
    }


    @Override
    public String toString() {
        return "ResultMessage{" +
                "messages=" + messages +
                ", channelType=" + channelType +
                ", ic=" + ic +
                '}';
    }
}
