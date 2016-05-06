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

package co.mafiagame.test;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.engine.api.GameApi;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.domain.Role;
import co.mafiagame.engine.executor.CommandExecutor;
import co.mafiagame.test.env.TestInterfaceContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
public class TestHelper {
    private final List<InterfaceContext> userInterfaceContexts = new ArrayList<>();
    private final List<Player> users;
    private final List<Player> mafia;
    private final List<Player> citizens;
    private final Player doctor;
    private final Player detector;
    private final String gameIdentity;
    private final GameContainer gameContainer;

    public TestHelper(GameContainer gameContainer, String gameIdentity, Game game) {
        this.gameContainer = gameContainer;
        this.gameIdentity = gameIdentity;
        game.getPlayers().forEach(p ->
                userInterfaceContexts.add(
                        new TestInterfaceContext(gameIdentity, p.getAccount().getUsername(), ChannelType.GENERAL)
                ));
        users = gameContainer.getGame(userInterfaceContexts.get(0)).getPlayers();
        citizens = users.stream()
                .filter(p -> p.getRole() == Role.CITIZEN)
                .collect(Collectors.toList());
        mafia = users.stream()
                .filter(p -> p.getRole() == Role.MAFIA)
                .collect(Collectors.toList());
        detector = users.stream()
                .filter(p -> p.getRole() == Role.DETECTOR).findFirst().get();
        doctor = users.stream()
                .filter(p -> p.getRole() == Role.DOCTOR).findFirst().get();
    }

    public TestHelper(CommandExecutor commandExecutor, GameApi gameApi,
                      GameContainer gameContainer, String gameIdentity,
                      int citizenNum, int mafiaNum, boolean hasDetector, boolean hasDoctor) throws InterruptedException {
        this.gameIdentity = gameIdentity;
        this.gameContainer = gameContainer;
        int playerNum = citizenNum + mafiaNum;
        if (hasDetector) playerNum++;
        if (hasDoctor) playerNum++;
        for (int i = 1; i <= playerNum; i++) {
            InterfaceContext cic = new TestInterfaceContext(gameIdentity, "user" + i, ChannelType.GENERAL);
            userInterfaceContexts.add(cic);
        }
        gameApi.startStashedGame(ic(), citizenNum, mafiaNum, hasDetector ? 1 : 0, hasDoctor ? 1 : 0);
        userInterfaceContexts.forEach(u ->
                gameApi.register(u, u.getUserName(), null, null)
        );
        commandExecutor.waitUntilOver(ic());
        users = gameContainer.getGame(userInterfaceContexts.get(0)).getPlayers();
        citizens = users.stream()
                .filter(p -> p.getRole() == Role.CITIZEN)
                .collect(Collectors.toList());
        mafia = users.stream()
                .filter(p -> p.getRole() == Role.MAFIA)
                .collect(Collectors.toList());
        if (hasDetector)
            detector = users.stream()
                    .filter(p -> p.getRole() == Role.DETECTOR).findFirst().get();
        else
            detector = null;
        if (hasDoctor)
            doctor = users.stream()
                    .filter(p -> p.getRole() == Role.DOCTOR).findFirst().get();
        else
            doctor = null;
    }

    public InterfaceContext ic() {
        return userInterfaceContexts.get(0);
    }

    public Game game() {
        return gameContainer.getGame(ic());
    }

    public InterfaceContext user(int index) {
        return aliveIc().get(index);
    }

    public String username(int index) {
        return user(index).getUserName();
    }

    public Player mafia(int index) {
        return mafia.get(index);
    }

    public String mafiaUsername(int index) {
        return mafia(index).getAccount().getUsername();
    }

    public InterfaceContext mafiaIc(int index, ChannelType type) {
        return new TestInterfaceContext(gameIdentity, mafiaUsername(index), type);
    }

    public InterfaceContext mafiaIc(int index) {
        return mafiaIc(index, ChannelType.USER_PRIVATE);
    }


    public Player citizen(int index) {
        return citizens.get(index);
    }

    public String citizenUsername(int index) {
        return citizen(index).getAccount().getUsername();
    }

    public InterfaceContext citizenIc(int index, ChannelType type) {
        return new TestInterfaceContext(gameIdentity, citizenUsername(index), type);
    }

    public InterfaceContext citizenIc(int index) {
        return citizenIc(index, ChannelType.USER_PRIVATE);
    }

    public String doctorUsername() {
        return doctor.getAccount().getUsername();
    }

    public InterfaceContext doctorIc(ChannelType type) {
        return new TestInterfaceContext(gameIdentity, doctorUsername(), type);
    }

    public InterfaceContext doctorIc() {
        return doctorIc(ChannelType.USER_PRIVATE);
    }

    public String detectorUsername() {
        return detector.getAccount().getUsername();
    }

    public InterfaceContext detectorIc(ChannelType type) {
        return new TestInterfaceContext(gameIdentity, detectorUsername(), type);
    }

    public InterfaceContext detectorIc() {
        return detectorIc(ChannelType.USER_PRIVATE);
    }

    public List<InterfaceContext> aliveIc() {
        return userInterfaceContexts.stream()
                .filter(i ->
                        game().getPlayers().stream()
                                .anyMatch(p -> p.getAccount().getUsername().equals(i.getUserName()))
                ).collect(Collectors.toList());
    }


    public List<Player> users() {
        return Collections.unmodifiableList(users);
    }

    public List<Player> mafia() {
        return Collections.unmodifiableList(mafia);
    }

    public List<Player> citizens() {
        return Collections.unmodifiableList(citizens);
    }

    public Player doctor() {
        return doctor;
    }

    public Player detector() {
        return detector;
    }

}
