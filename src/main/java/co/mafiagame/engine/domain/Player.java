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

package co.mafiagame.engine.domain;

import co.mafiagame.common.domain.Account;

/**
 * @author hekmatof
 */
public class Player {
    private final Account account;
    private Role role = Role.UNKNOWN;
    private boolean voted = false;

    public boolean checkUsername(String username) {
        return this.account.getUsername().equalsIgnoreCase(username);
    }

    public Player(Account account) {
        this.account = account;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public Account getAccount() {
        return account;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        if (account != null ? !account.equals(player.account) : player.account != null) return false;
        return role == player.role;

    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "account=" + account +
                ", role=" + role +
                ", voted=" + voted +
                '}';
    }
}
