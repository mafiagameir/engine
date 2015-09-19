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

package co.mafiagame.engine.command.context;

import co.mafiagame.common.channel.InterfaceContext;

/**
 * @author hekmatof
 */
public class StartStashedGameCommandContext extends CommandContext {
    private final int citizenNum;
    private final int mafiaNum;
    private final int detectorNum;
    private final int doctorNum;

    public StartStashedGameCommandContext(InterfaceContext interfaceContext,
                                          int citizenNum, int mafiaNum,
                                          int detectorNum, int doctorNum) {
        super(interfaceContext);
        this.citizenNum = citizenNum;
        this.mafiaNum = mafiaNum;
        this.detectorNum = detectorNum;
        this.doctorNum = doctorNum;
    }

    public int getCitizenNum() {
        return citizenNum;
    }

    public int getMafiaNum() {
        return mafiaNum;
    }

    public int getDetectorNum() {
        return detectorNum;
    }

    public int getDoctorNum() {
        return doctorNum;
    }

    @Override
    public String toString() {
        return "StartStashedGameCommandContext{" + "citizenNum=" + citizenNum
                + ", mafiaNum=" + mafiaNum + ", detectorNum=" + detectorNum
                + ", doctorNum=" + doctorNum + '}';
    }
}
