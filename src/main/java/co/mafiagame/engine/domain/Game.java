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

import co.mafiagame.common.Constants;
import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.result.Option;
import co.mafiagame.engine.exception.PlayerNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A game on the table
 *
 * @author hekmatof
 */
public class Game implements InterfaceContextAware {

    private Date createdDate;
    private Date lastUpdate;
    private InterfaceContext interfaceContext;
    private GameState gameState;
    private GameMood gameMood;
    private List<Player> players;
    private Map<Player, List<Player>> playerVote = new HashMap<>();
    private ElectionMood electionMood = ElectionMood.NONE;
    private Player killCandidate;
    private Player healCandidate;
    private boolean tellGameState = false;
    private Map<String, Role> backupPlayerState = new HashMap<>();
    private Set<Player> cancelPlayers = new HashSet<>();

    public Game(StashedGame stashedGame) {
        this.createdDate = new Date();
        this.lastUpdate = new Date();
        this.interfaceContext = stashedGame.getInterfaceContext();
        this.gameState = stashedGame.getGameState();
        this.players = stashedGame.getPlayers();
        this.gameMood = GameMood.DAY;// on the first night no one killed
    }

    public Game() {
    }

    public synchronized void update() {
        this.lastUpdate = new Date();
    }

    public synchronized GameMood nextMode() {
        switch (gameMood) {
            case DAY:
                return GameMood.NIGHT_MAFIA;
            case NIGHT_MAFIA:
                if (gameState.getDetectiveNum() > 0)
                    return GameMood.NIGHT_DETECTIVE;
            case NIGHT_DETECTIVE:
                if (gameState.getDoctorNum() > 0)
                    return GameMood.NIGHT_DOCTOR;
            default:
                return GameMood.DAY;
        }
    }

    public synchronized boolean checkElectionIsOver() {
        for (Player player : players)
            if (!player.isVoted())
                return false;
        return true;
    }

    public synchronized boolean checkMafiaElectionIsOver() {
        List<Player> mafias = mafias();
        for (Player mafia : mafias)
            if (!mafia.isVoted())
                return false;
        return true;

    }

    public synchronized void startElection(boolean finalElection) {
        clearElection();
        electionMood = finalElection ? ElectionMood.FINALELECTION : ElectionMood.ELECTION;
    }

    public synchronized void clearElection() {
        playerVote.clear();
        players.stream().forEach(p -> p.setVoted(false));
        electionMood = ElectionMood.NONE;
    }

    public synchronized void killPlayer(Player player) {
        if (player == null)
            return;
        switch (player.getRole()) {
            case MAFIA:
                gameState.killMafia();
                break;
            case CITIZEN:
                gameState.killCitizen();
                break;
            case DETECTIVE:
                gameState.killDetective();
                break;
            case DOCTOR:
                gameState.killDoctor();
                break;
        }
        players.remove(player);
    }

    public synchronized GameResult checkGameOver() {
        int citizenNum = gameState.getCitizenNum();
        int doctorNum = gameState.getDoctorNum();
        int detectiveNum = gameState.getDetectiveNum();
        int mafiaNum = gameState.getMafiaNum();
        int citizenSum = citizenNum + doctorNum + detectiveNum;
        GameResult gameResult;
        if (cancelPlayers.size() >= (players.size() / 2) + 1)
            gameResult = GameResult.CANCELED;
        else if (mafiaNum >= citizenSum)
            gameResult = GameResult.MAFIAS_WIN;
        else if (mafiaNum == 0)
            gameResult = GameResult.CITIZEN_WIN;
        else
            gameResult = GameResult.UNKNOWN;
        return gameResult;
    }

    public synchronized void temporaryKillPlayer(Player player) {
        killCandidate = player;
    }

    public synchronized Player playerByUsername(String username) {
        try {
            return players.stream().filter(p -> p.checkUsername(username)).findFirst().get();
        } catch (NoSuchElementException e) {
            throw new PlayerNotFoundException(username);
        }
    }

    public synchronized List<Player> mafias() {
        return this.players.stream().filter(p -> p.getRole() == Role.MAFIA).collect(Collectors.toList());
    }

    public synchronized Player detective() {
        return this.players.stream().filter(p -> p.getRole() == Role.DETECTIVE).findFirst().get();
    }

    public synchronized Player doctor() {
        return this.players.stream().filter(p -> p.getRole() == Role.DOCTOR).findFirst().get();
    }

    public synchronized List<Option> makeOption(String command, boolean withNobody) {
        List<Option> playerList = this.getPlayers().stream().map(Player::getAccount)
                .map(Account::getUsername).map(u -> new Option(command, u)).collect(Collectors.toList());
        if (withNobody)
            playerList.add(new Option(command, Constants.NO_BODY));
        return playerList;
    }

    public synchronized InterfaceContext getInterfaceContext() {
        return interfaceContext;
    }

    public synchronized GameState getGameState() {
        return gameState;
    }

    public synchronized List<Player> getPlayers() {
        return players;
    }

    public synchronized GameMood getGameMood() {
        return gameMood;
    }

    public synchronized Map<Player, List<Player>> getPlayerVote() {
        return playerVote;
    }

    public synchronized void setGameMood(GameMood gameMood) {
        this.gameMood = gameMood;
    }

    public synchronized ElectionMood getElectionMood() {
        return electionMood;
    }

    public synchronized Player getKillCandidate() {
        return killCandidate;
    }

    public synchronized void clearKillCandidate() {
        this.killCandidate = null;
    }

    public synchronized Player getHealCandidate() {
        return healCandidate;
    }

    public synchronized void setHealCandidate(Player healCandidate) {
        this.healCandidate = healCandidate;
    }

    public synchronized boolean isTellGameState() {
        return tellGameState;
    }

    public synchronized void toggleTellGameState() {
        this.tellGameState = !this.tellGameState;
    }

    public synchronized Map<String, Role> getBackupPlayerState() {
        return backupPlayerState;
    }

    public synchronized Set<Player> getCancelPlayers() {
        return cancelPlayers;
    }

    public synchronized void assignBackupPlayers() {
        players.stream().forEach(p -> backupPlayerState.put(p.getAccount().getUsername(), p.getRole()));
    }

    public synchronized Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String toString() {
        return "Game{" +
                "createdDate=" + createdDate +
                ", lastUpdate=" + lastUpdate +
                ", interfaceContext=" + interfaceContext +
                ", gameState=" + gameState +
                ", gameMood=" + gameMood +
                ", players=" + players +
                ", playerVote=" + playerVote +
                ", electionMood=" + electionMood +
                ", killCandidate=" + killCandidate +
                ", healCandidate=" + healCandidate +
                ", tellGameState=" + tellGameState +
                ", backupPlayerState=" + backupPlayerState +
                ", cancelPlayers=" + cancelPlayers +
                '}';
    }
}
