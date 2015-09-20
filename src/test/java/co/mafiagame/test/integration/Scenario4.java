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

package co.mafiagame.test.integration;

import co.mafiagame.common.configuration.CommonConfiguration;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.engine.api.GameApi;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.domain.Role;
import co.mafiagame.engine.executor.CommandExecutor;
import co.mafiagame.test.env.TestInterfaceChannel;
import co.mafiagame.test.env.TestInterfaceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author nazila
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonConfiguration.class)
public class Scenario4 {

    @Autowired
    private GameApi gameApi;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private TestInterfaceChannel testInterfaceChannel;
    private static final String gameIdentity = "game1";

    private final TestInterfaceContext naziIc = new TestInterfaceContext(
            gameIdentity, "nazi", ChannelType.GENERAL);
    private final TestInterfaceContext esaIc = new TestInterfaceContext(
            gameIdentity, "esa", ChannelType.GENERAL);
    private final TestInterfaceContext hamidIc = new TestInterfaceContext(
            gameIdentity, "hamid", ChannelType.GENERAL);
    private final TestInterfaceContext maryamIc = new TestInterfaceContext(
            gameIdentity, "maryam", ChannelType.GENERAL);
    private final TestInterfaceContext ic = new TestInterfaceContext(
            gameIdentity, "starter", ChannelType.GENERAL);
    private String mafia1 = null;
    private String detector = null;
    private String doctor = null;
    private String citizen1 = null;
    private TestInterfaceContext icMafia1 = null;
    private TestInterfaceContext icDetector = null;
    private TestInterfaceContext icDoctor = null;
    private TestInterfaceContext icCitizen1 = null;

    @Test
    public void test() throws InterruptedException {
        testInterfaceChannel.setPrint(true);
        startGame();
        commandExecutor.waitUntilOver(ic);
        assertEquals(GameMood.DAY, gameContainer.getGame(ic).getGameMood());
        assertEquals(4, gameContainer.getGame(ic).getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));
        setValues();
        OneUserCancelGame();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("user.canceled.game"));
        finalElection();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("final.election.started"));
        EveryOneCancelGame();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("game.canceled"));
        finalElection();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("game.not.started.yet"));
    }

    public void startGame() throws InterruptedException {

        gameApi.startStashedGame(naziIc, 1, 1, 1, 1);
        gameApi.register(naziIc, "nazi",null,null);
        gameApi.register(esaIc, "esa",null,null);
        gameApi.register(hamidIc, "hamid",null,null);
        gameApi.register(maryamIc, "maryam",null,null);
        commandExecutor.waitUntilOver(ic);
    }

    public void OneUserCancelGame() {
        gameApi.cancelGame(icCitizen1, citizen1);
    }

    private void setValues() {
        List<Player> mafia = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.MAFIA)
                .collect(Collectors.toList());
        mafia1 = mafia.get(0).getAccount().getUsername();
        detector = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.DETECTOR).findFirst().get()
                .getAccount().getUsername();
        doctor = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.DOCTOR).findFirst().get()
                .getAccount().getUsername();
        icMafia1 = new TestInterfaceContext(gameIdentity, mafia.get(0)
                .getAccount().getUsername(), ChannelType.USER_PRIVATE);
        icDetector = new TestInterfaceContext(gameIdentity, detector,
                ChannelType.USER_PRIVATE);
        icDoctor = new TestInterfaceContext(gameIdentity, doctor,
                ChannelType.USER_PRIVATE);
        List<Player> citizen = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.CITIZEN)
                .collect(Collectors.toList());
        citizen1 = citizen.get(0).getAccount().getUsername();
        icCitizen1 = new TestInterfaceContext(gameIdentity, citizen1,
                ChannelType.USER_PRIVATE);

    }

    private void EveryOneCancelGame() {
        gameApi.cancelGame(icCitizen1, citizen1);
        gameApi.cancelGame(icMafia1, mafia1);
        gameApi.cancelGame(icDetector, detector);
        gameApi.cancelGame(icDoctor, doctor);
    }

    private void finalElection() {
        TestInterfaceContext ic = new TestInterfaceContext(gameIdentity,
                "hamid", ChannelType.GENERAL);
        gameApi.startFinalElection(ic);

    }
}
