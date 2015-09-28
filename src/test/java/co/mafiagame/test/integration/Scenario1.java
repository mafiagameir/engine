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

import co.mafiagame.common.Constants;
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
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author hekmatof
 * @author nazila
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonConfiguration.class)
@Component
public class Scenario1 {
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
    private final TestInterfaceContext moradIc = new TestInterfaceContext(
            gameIdentity, "morad", ChannelType.GENERAL);
    private final TestInterfaceContext shahinIc = new TestInterfaceContext(
            gameIdentity, "shahin", ChannelType.GENERAL);
    private final TestInterfaceContext hassanId = new TestInterfaceContext(
            gameIdentity, "hassan", ChannelType.GENERAL);
    private final TestInterfaceContext raziIc = new TestInterfaceContext(
            gameIdentity, "razi", ChannelType.GENERAL);
    private final TestInterfaceContext zibaIc = new TestInterfaceContext(
            gameIdentity, "ziba", ChannelType.GENERAL);
    private final TestInterfaceContext khalilIc = new TestInterfaceContext(
            gameIdentity, "khalil", ChannelType.GENERAL);
    private final TestInterfaceContext ic = new TestInterfaceContext(
            gameIdentity, "starter", ChannelType.GENERAL);
    private String mafia1 = null;
    private String mafia2 = null;
    private String detector = null;
    private String doctor = null;
    private String citizen1 = null;
    private String citizen2 = null;
    private String citizen3 = null;
    private String citizen4 = null;
    private String citizen5 = null;
    private String citizen6 = null;
    private TestInterfaceContext icMafia1 = null;
    private TestInterfaceContext icMafia2 = null;
    private TestInterfaceContext icDetector = null;
    private TestInterfaceContext icDoctor = null;
    private TestInterfaceContext icCitizen1 = null;
    private TestInterfaceContext icCitizen2 = null;
    private TestInterfaceContext icCitizen3 = null;
    private TestInterfaceContext icCitizen4 = null;
    private TestInterfaceContext icCitizen5 = null;
    private TestInterfaceContext icCitizen6 = null;

    private void setValues() {
        List<Player> mafia = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.MAFIA)
                .collect(Collectors.toList());
        mafia1 = mafia.get(0).getAccount().getUsername();
        mafia2 = mafia.get(1).getAccount().getUsername();
        detector = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.DETECTOR).findFirst().get()
                .getAccount().getUsername();
        doctor = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.DOCTOR).findFirst().get()
                .getAccount().getUsername();
        icMafia1 = new TestInterfaceContext(gameIdentity, mafia.get(0)
                .getAccount().getUsername(), ChannelType.USER_PRIVATE);
        icMafia2 = new TestInterfaceContext(gameIdentity, mafia.get(1)
                .getAccount().getUsername(), ChannelType.USER_PRIVATE);
        icDetector = new TestInterfaceContext(gameIdentity, detector,
                ChannelType.USER_PRIVATE);
        icDoctor = new TestInterfaceContext(gameIdentity, doctor,
                ChannelType.USER_PRIVATE);
        List<Player> citizen = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.CITIZEN)
                .collect(Collectors.toList());
        citizen1 = citizen.get(0).getAccount().getUsername();
        citizen2 = citizen.get(1).getAccount().getUsername();
        citizen3 = citizen.get(2).getAccount().getUsername();
        citizen4 = citizen.get(3).getAccount().getUsername();
        citizen5 = citizen.get(4).getAccount().getUsername();
        citizen6 = citizen.get(5).getAccount().getUsername();
        icCitizen1 = new TestInterfaceContext(gameIdentity, citizen1,
                ChannelType.USER_PRIVATE);
        icCitizen2 = new TestInterfaceContext(gameIdentity, citizen2,
                ChannelType.USER_PRIVATE);
        icCitizen3 = new TestInterfaceContext(gameIdentity, citizen3,
                ChannelType.USER_PRIVATE);
        icCitizen4 = new TestInterfaceContext(gameIdentity, citizen4,
                ChannelType.USER_PRIVATE);
        icCitizen5 = new TestInterfaceContext(gameIdentity, citizen5,
                ChannelType.USER_PRIVATE);
        icCitizen6 = new TestInterfaceContext(gameIdentity, citizen6,
                ChannelType.USER_PRIVATE);

    }

    @Test
    public void test() throws InterruptedException {
        startGame();
        commandExecutor.waitUntilOver(ic);
        assertEquals(GameMood.DAY, gameContainer.getGame(ic).getGameMood());
        assertEquals(10, gameContainer.getGame(ic).getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));
        setValues();
        firstElection();
        userVotes();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("election.started"));
        finalElectionRound1();
        userVotes();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));
        mafiaVote();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));
        detectorAsk();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.role.is.not.mafia"));
        doctorheal();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("ok"));
        finalElectionRound2();
        secondUserVote();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("election.started"));
        secondMafiaVote();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));
        doctorHealRound2();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("ok"));
        whoIsPlaying();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("user.playing"));
        finalElectionRound2();
        thirdUserVote();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));
        thirdMafiaVote();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("mafia.win"));
        testInterfaceChannel.printMessages();
    }

    private void whoIsPlaying() {
        gameApi.whoIsPlaying(icMafia1);
    }

    public void startGame() throws InterruptedException {

        gameApi.startStashedGame(naziIc, 6, 2, 1, 1);
        gameApi.register(naziIc, "nazi", null, null);
        gameApi.register(esaIc, "esa", null, null);
        gameApi.register(hamidIc, "hamid", null, null);
        gameApi.register(maryamIc, "maryam", null, null);
        gameApi.register(moradIc, "morad", null, null);
        gameApi.register(shahinIc, "shahin", null, null);
        gameApi.register(hassanId, "hassan", null, null);
        gameApi.register(raziIc, "razi", null, null);
        gameApi.register(zibaIc, "ziba", null, null);
        gameApi.register(khalilIc, "khalil", null, null);
        commandExecutor.waitUntilOver(ic);
    }

    public void firstElection() throws InterruptedException {
        TestInterfaceContext ic = new TestInterfaceContext(gameIdentity, "esa",
                ChannelType.GENERAL);
        gameApi.startElection(ic);
    }

    private void finalElectionRound1() {
        TestInterfaceContext ic = new TestInterfaceContext(gameIdentity,
                "hamid", ChannelType.GENERAL);
        gameApi.startFinalElection(ic);

    }

    private void finalElectionRound2() {
        TestInterfaceContext ic = new TestInterfaceContext(gameIdentity,
                mafia1, ChannelType.GENERAL);
        gameApi.startFinalElection(ic);

    }

    private void userVotes() {
        List<String> victims = new ArrayList<>();
        victims.add(mafia1);
        victims.add(doctor);
        victims.add(citizen1);
        gameApi.vote(naziIc, "nazi", Collections.singletonList(citizen3));
        gameApi.vote(esaIc, "esa", Collections.singletonList(citizen3));
        gameApi.vote(hamidIc, "hamid", Collections.singletonList(citizen4));
        gameApi.vote(maryamIc, "maryam", Collections.singletonList(citizen3));
        gameApi.vote(moradIc, "morad", Collections.singletonList(citizen3));
        gameApi.vote(shahinIc, "shahin", Collections.singletonList(citizen6));
        gameApi.vote(hassanId, "hassan", victims);
        gameApi.vote(raziIc, "razi", Collections.singletonList(citizen5));
        gameApi.vote(khalilIc, "khalil", Collections.singletonList(citizen1));
        gameApi.vote(zibaIc, "ziba", Collections.singletonList(detector));
    }

    private void mafiaVote() {
        gameApi.mafiaKillVote(icMafia1, mafia1, citizen1);
        gameApi.mafiaKillVote(icMafia2, mafia2, citizen1);
    }

    private void detectorAsk() {
        gameApi.detectorAsk(icDetector, detector, citizen6);
    }

    private void doctorheal() {
        gameApi.doctorHeal(icDoctor, doctor, citizen2);
    }

    private void secondUserVote() {
        gameApi.vote(icCitizen2, citizen2,
                Collections.singletonList(Constants.NO_BODY));
        gameApi.vote(icCitizen4, citizen4, Collections.singletonList(detector));
        gameApi.vote(icCitizen5, citizen5, Collections.singletonList(detector));
        gameApi.vote(icCitizen6, citizen6, Collections.singletonList(detector));
        gameApi.vote(icMafia1, mafia1, Collections.singletonList(detector));
        gameApi.vote(icMafia2, mafia2, Collections.singletonList(citizen6));
        gameApi.vote(icDetector, detector, Collections.singletonList(detector));
        gameApi.vote(icDoctor, doctor, Collections.singletonList(citizen2));
    }

    private void secondMafiaVote() {
        gameApi.mafiaKillVote(icMafia1, mafia1, Constants.NO_BODY);
        gameApi.mafiaKillVote(icMafia2, mafia2, doctor);
    }

    private void doctorHealRound2() {
        gameApi.doctorHeal(icDoctor, doctor, mafia2);
    }

    private void thirdUserVote() {
        gameApi.vote(icCitizen2, citizen2, Collections.singletonList(citizen2));
        gameApi.vote(icCitizen4, citizen4, Collections.singletonList(citizen2));
        gameApi.vote(icCitizen5, citizen5, Collections.singletonList(citizen2));
        gameApi.vote(icCitizen6, citizen6, Collections.singletonList(citizen2));
        gameApi.vote(icMafia1, mafia1, Collections.singletonList(mafia2));
        gameApi.vote(icMafia2, mafia2, Collections.singletonList(citizen2));
    }

    private void thirdMafiaVote() {
        gameApi.mafiaKillVote(icMafia1, mafia1, citizen6);
        gameApi.mafiaKillVote(icMafia2, mafia2, citizen6);
    }
}
