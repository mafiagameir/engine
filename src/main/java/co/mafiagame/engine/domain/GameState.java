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

/**
 * @author hekmatof
 */
public class GameState {
    private int citizenNum;
    private int mafiaNum;
    private int detectiveNum;
    private int doctorNum;

    public GameState() {
    }

    public GameState(int citizenNum, int mafiaNum, int detectiveNum,
                     int doctorNum) {
        this.citizenNum = citizenNum;
        this.mafiaNum = mafiaNum;
        this.detectiveNum = detectiveNum;
        this.doctorNum = doctorNum;
    }

    public synchronized void killMafia() {
        mafiaNum--;
    }

    public synchronized void killCitizen() {
        citizenNum--;
    }

    public synchronized void killDetective() {
        detectiveNum--;
    }

    public synchronized void killDoctor() {
        doctorNum--;
    }

    public synchronized int totalPlayer() {
        return citizenNum + mafiaNum + detectiveNum + doctorNum;
    }

    public synchronized int getCitizenNum() {
        return citizenNum;
    }

    public synchronized int getMafiaNum() {
        return mafiaNum;
    }

    public synchronized int getDetectiveNum() {
        return detectiveNum;
    }

    public synchronized int getDoctorNum() {
        return doctorNum;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "citizenNum=" + citizenNum +
                ", mafiaNum=" + mafiaNum +
                ", detectiveNum=" + detectiveNum +
                ", doctorNum=" + doctorNum +
                '}';
    }
}
