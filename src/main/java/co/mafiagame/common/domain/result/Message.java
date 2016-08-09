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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author nazila
 */
public class Message {

    private final String messageCode;
    private String[] args = new String[0];
    private String receiverId;
    private List<Option> options = new ArrayList<>();
    private List<String> toUsers = new ArrayList<>();

    public Message(String messageCode) {
        this.messageCode = messageCode;
    }

    public List<String> getToUsers() {
        return toUsers;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String[] getArgs() {
        return args;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public List<Option> getOptions() {
        return options;
    }

    public Message setArgs(String... args) {
        this.args = args;
        return this;
    }

    public Message setReceiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public Message setOptions(List<Option> options) {
        this.options = options;
        return this;
    }

    public Message setToUsers(List<String> toUsers) {
        this.toUsers = toUsers;
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageCode='" + messageCode + '\'' +
                ", args=" + Arrays.toString(args) +
                ", receiverId='" + receiverId + '\'' +
                ", options=" + options +
                ", toUsers=" + toUsers +
                '}';
    }
}
