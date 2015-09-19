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

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.common.domain.InterfaceType;
import co.mafiagame.common.domain.result.ChannelType;

/**
 * @author hekmatof
 */
public class TestInterfaceContext implements InterfaceContext {
    private final String gameIdentity;
    private final ChannelType senderType;
    private final String userId;

    public TestInterfaceContext(String gameIdentity, String userId, ChannelType senderType) {
        this.gameIdentity = gameIdentity;
        this.senderType = senderType;
        this.userId = userId;
    }

    @Override
    public InterfaceType interfaceType() {
        return InterfaceType.TEST;
    }

    @Override
    public ChannelType getSenderType() {
        return senderType;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getRoomId() {
        return gameIdentity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestInterfaceContext that = (TestInterfaceContext) o;

        return gameIdentity.equals(that.gameIdentity);

    }

    @Override
    public int hashCode() {
        return gameIdentity.hashCode();
    }

    @Override
    public String toString() {
        return "TestInterfaceContext{" +
                "gameIdentity='" + gameIdentity + '\'' +
                ", senderType=" + senderType +
                '}';
    }
}
