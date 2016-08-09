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

/**
 * @author hekmatof
 */
public class Option {
    private final String command;
    private final String arg;

    public Option(String command, String arg) {
        this.command = command;
        this.arg = arg;
    }

    public Option(String command) {
        this.command = command;
        this.arg = "";
    }

    public String getCommand() {
        return command;
    }

    public String getArg() {
        return arg;
    }


    @Override
    public String toString() {
        return "Option{" +
                "command='" + command + '\'' +
                ", arg='" + arg + '\'' +
                '}';
    }
}
