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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author nazila
 * @author hekmatof
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonConfiguration.class)
public class Scenario2 {
    @Autowired
    private GameApi gameApi;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private TestInterfaceChannel testInterfaceChannel;

    private static final String gameIdentity = "game2";

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
    private String mafia3 = null;
    private String detector = null;
    private String doctor = null;
    private String citizen1 = null;
    private String citizen2 = null;
    private String citizen3 = null;
    private String citizen4 = null;
    private String citizen5 = null;
    private TestInterfaceContext icMafia1 = null;
    private TestInterfaceContext icMafia2 = null;
    private TestInterfaceContext icMafia3 = null;
    private TestInterfaceContext icDetector = null;
    private TestInterfaceContext icDoctor = null;
    private TestInterfaceContext icCitizen1 = null;
    private TestInterfaceContext icCitizen2 = null;
    private TestInterfaceContext icCitizen3 = null;
    private TestInterfaceContext icCitizen4 = null;
    private TestInterfaceContext icCitizen5 = null;
    private TestInterfaceContext icGeneralMafia = null;
    private TestInterfaceContext icGeneralCitizen = null;

    private void setValues() {
        List<Player> mafia = gameContainer.getGame(ic).getPlayers().stream()
                .filter(p -> p.getRole() == Role.MAFIA)
                .collect(Collectors.toList());
        mafia1 = mafia.get(0).getAccount().getUsername();
        mafia2 = mafia.get(1).getAccount().getUsername();
        mafia3 = mafia.get(2).getAccount().getUsername();
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
        icMafia3 = new TestInterfaceContext(gameIdentity, mafia.get(2)
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
        icGeneralMafia = new TestInterfaceContext(gameIdentity, mafia1,
                ChannelType.GENERAL);
        icGeneralCitizen = new TestInterfaceContext(gameIdentity, citizen2,
                ChannelType.GENERAL);

    }

    @Test
    public void test() throws InterruptedException {
        testInterfaceChannel.setPrint(true);
        startGameHalf();
        commandExecutor.waitUntilOver(ic);
        assertEquals(null, gameContainer.getGame(ic));
        commandExecutor.waitUntilOver(ic);
        firstElection();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("game.not.started.yet"));
        startGameFinal();
        commandExecutor.waitUntilOver(ic);
        assertEquals(GameMood.DAY, gameContainer.getGame(ic).getGameMood());
        assertEquals(10, gameContainer.getGame(ic).getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));
        finalElectionRound1();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("final.election.started"));
        setValues();
        userVotes();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));
        citizenKill();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.mafia.vote"));
        mafiaKillOnDay();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.mafia.vote"));
        userVotes();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("no.election.started"));
        detectorAsk();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("you.cant.ask.now"));
        doctorHeal();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.doctor.heal"));
        finalElectionRound1();
        finalElectionRound1();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("final.election.started"));
        userVotesNobodyAndPlayerNotExist();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("player.not.found"));
        userVotesCorrect();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("player.was.killed.with.maximum.votes"));
        finalElectionRound1();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("vote.on.night.not.allowed"));
        userVotesCorrect();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("vote.on.night.not.allowed"));
        citizenKillOnNight();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("you.are.not.mafia"));
        detectorAsk();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("you.cant.ask.now"));
        doctorHeal();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.doctor.heal"));
        mafiaKillVote();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));
        detectorAskInGeneral();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel
                .lastKeyIs("command.is.unavailable.here"));
        citizenAskBesideDetector();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("you.are.not.detector"));
        detectorAsk();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.containKey("user.role.is.mafia"));
        citizenHealBesideDoctor();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel.lastKeyIs("you.are.not.doctor"));
        doctorHealInGeneral();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel
                .lastKeyIs("you.are.not.in.doctor.private"));
        doctorHeal();
        commandExecutor.waitUntilOver(ic);
        assertTrue(testInterfaceChannel
                .containKey("ok"));
        testInterfaceChannel.printMessages();

    }

    public void startGameHalf() throws InterruptedException {

        gameApi.startStashedGame(naziIc, 5, 3, 1, 1);
        gameApi.register(naziIc, "nazi", null, null);
        gameApi.register(esaIc, "esa", null, null);
        gameApi.register(hamidIc, "hamid", null, null);
        gameApi.register(maryamIc, "maryam", null, null);
        gameApi.register(moradIc, "morad", null, null);
        gameApi.register(shahinIc, "shahin", null, null);
        gameApi.register(hassanId, "hassan", null, null);
        gameApi.register(raziIc, "razi", null, null);
        gameApi.register(zibaIc, "ziba", null, null);
        commandExecutor.waitUntilOver(ic);
    }

    public void startGameFinal() throws InterruptedException {

        gameApi.startStashedGame(naziIc, 5, 3, 1, 1);
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

    private void userVotes() {
        List<String> victims = new ArrayList<>();
        victims.add(mafia1);
        victims.add(doctor);
        victims.add(citizen1);
        gameApi.vote(naziIc, "nazi", Collections.singletonList(citizen3));
        gameApi.vote(esaIc, "esa", Collections.singletonList(citizen3));
        gameApi.vote(hamidIc, "hamid", Collections.singletonList(citizen1));
        gameApi.vote(maryamIc, "maryam", Collections.singletonList(citizen3));
        gameApi.vote(moradIc, "morad", Collections.singletonList(citizen3));
        gameApi.vote(shahinIc, "shahin", Collections.singletonList(citizen3));
        gameApi.vote(hassanId, "hassan", victims);
        gameApi.vote(raziIc, "razi", Collections.singletonList(citizen1));
        gameApi.vote(khalilIc, "khalil", Collections.singletonList(citizen1));
        gameApi.vote(zibaIc, "ziba", Collections.singletonList(citizen1));
    }

    private void userVotesNobodyAndPlayerNotExist() {
        List<String> victims = new ArrayList<>();
        victims.add(mafia1);
        victims.add(doctor);
        victims.add(citizen1);
        gameApi.vote(icCitizen1, citizen1, Collections.singletonList(citizen1));
        gameApi.vote(icGeneralCitizen, citizen2,
                Collections.singletonList(citizen1));
        gameApi.vote(icCitizen3, citizen3, Collections.singletonList(citizen1));
        gameApi.vote(icCitizen4, citizen4, Collections.singletonList(citizen3));
        gameApi.vote(icCitizen5, citizen5, Collections.singletonList(citizen1));
        gameApi.vote(icDetector, detector, Collections.singletonList(citizen1));
        gameApi.vote(icDoctor, doctor, victims);
        gameApi.vote(icMafia1, mafia1, Collections.singletonList(citizen1));
        gameApi.vote(icMafia2, mafia2, Collections.singletonList("soghra"));
        gameApi.vote(icMafia3, mafia3, Collections.singletonList(Constants.NO_BODY));
    }

    private void userVotesCorrect() {
        List<String> victims = new ArrayList<>();
        victims.add(mafia1);
        victims.add(doctor);
        victims.add(citizen1);
        gameApi.vote(icCitizen1, citizen1, Collections.singletonList(citizen1));
        gameApi.vote(icGeneralCitizen, citizen2,
                Collections.singletonList(citizen1));
        gameApi.vote(icCitizen3, citizen3, Collections.singletonList(citizen1));
        gameApi.vote(icCitizen4, citizen4, Collections.singletonList(citizen3));
        gameApi.vote(icCitizen5, citizen5, Collections.singletonList(citizen1));
        gameApi.vote(icDetector, detector, Collections.singletonList(citizen1));
        gameApi.vote(icDoctor, doctor, victims);
        gameApi.vote(icMafia1, mafia1, Collections.singletonList(citizen1));
        gameApi.vote(icMafia2, mafia2, Collections.singletonList(citizen1));
        gameApi.vote(icMafia3, mafia3, Collections.singletonList(Constants.NO_BODY));
    }

    private void citizenKill() {
        gameApi.mafiaKillVote(icGeneralCitizen, citizen2, mafia1);
    }

    private void mafiaKillOnDay() {
        gameApi.mafiaKillVote(icGeneralMafia, mafia1, citizen3);
    }

    private void citizenKillOnNight() {
        gameApi.mafiaKillVote(icGeneralCitizen, citizen2, mafia1);
    }

    private void detectorAsk() {
        gameApi.detectorAsk(icDetector, detector, mafia1);
    }

    private void mafiaKillVote() {
        gameApi.mafiaKillVote(icMafia1, mafia1, citizen2);
        gameApi.mafiaKillVote(icMafia2, mafia2, citizen2);
        gameApi.mafiaKillVote(icMafia3, mafia3, citizen3);
    }

    private void detectorAskInGeneral() {
        gameApi.detectorAsk(icGeneralCitizen, detector, mafia1);
    }

    private void citizenAskBesideDetector() {
        gameApi.detectorAsk(icCitizen2, citizen2, mafia1);
    }

    private void citizenHealBesideDoctor() {
        gameApi.doctorHeal(icDetector, detector, citizen2);
    }

    private void doctorHealInGeneral() {
        gameApi.doctorHeal(icGeneralCitizen, doctor, citizen2);
    }

    private void doctorHeal() {
        gameApi.doctorHeal(icDoctor, doctor, citizen2);
    }
}
