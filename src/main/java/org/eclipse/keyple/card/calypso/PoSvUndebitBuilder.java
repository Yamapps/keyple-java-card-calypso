/* **************************************************************************************
 * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.card.calypso;

import org.eclipse.keyple.card.calypso.po.PoRevision;
import org.eclipse.keyple.core.card.ApduRequest;
import org.eclipse.keyple.core.card.ApduResponse;

/**
 * (package-private)<br>
 * Builds the SV Undebit command.
 *
 * <p>Note: {@link PoSvUndebitBuilder} and {@link PoSvDebitBuilder} shares the same parser {@link
 * PoSvDebitParser}
 *
 * @since 2.0
 */
final class PoSvUndebitBuilder extends AbstractPoCommandBuilder<PoSvUndebitParser> {

  /** The command. */
  private static final PoCommand command = PoCommand.SV_UNDEBIT;

  private final PoClass poClass;
  private final PoRevision poRevision;
  /** apdu data array */
  private final byte[] dataIn;

  /**
   * Instantiates a new PoSvUndebitBuilder.
   *
   * @param poClass indicates which CLA byte should be used for the Apdu.
   * @param poRevision the PO revision.
   * @param amount amount to undebit (positive integer from 0 to 32767).
   * @param kvc the KVC.
   * @param date debit date (not checked by the PO).
   * @param time debit time (not checked by the PO).
   * @throws IllegalArgumentException - if the command is inconsistent
   * @since 2.0
   */
  public PoSvUndebitBuilder(
      PoClass poClass, PoRevision poRevision, int amount, byte kvc, byte[] date, byte[] time) {
    super(command);

    /*
     * @see Calypso Layer ID 8.02 (200108)
     * @see Ticketing Layer Recommendations 170 (200108)
     */
    if (amount < 0 || amount > 32767) {
      throw new IllegalArgumentException(
          "Amount is outside allowed boundaries (0 <= amount <= 32767)");
    }
    if (date == null || time == null) {
      throw new IllegalArgumentException("date and time cannot be null");
    }
    if (date.length != 2 || time.length != 2) {
      throw new IllegalArgumentException("date and time must be 2-byte arrays");
    }

    // keeps a copy of these fields until the builder is finalized
    this.poRevision = poRevision;
    this.poClass = poClass;

    // handle the dataIn size with signatureHi length according to PO revision (3.2 rev have a
    // 10-byte signature)
    dataIn = new byte[15 + (poRevision == PoRevision.REV3_2 ? 10 : 5)];

    // dataIn[0] will be filled in at the finalization phase.
    short amountShort = (short) amount;
    dataIn[1] = (byte) ((amountShort >> 8) & 0xFF);
    dataIn[2] = (byte) (amountShort & 0xFF);
    dataIn[3] = date[0];
    dataIn[4] = date[1];
    dataIn[5] = time[0];
    dataIn[6] = time[1];
    dataIn[7] = kvc;
    // dataIn[8]..dataIn[8+7+sigLen] will be filled in at the finalization phase.
  }

  /**
   * Complete the construction of the APDU to be sent to the PO with the elements received from the
   * SAM:
   *
   * <p>4-byte SAM id
   *
   * <p>3-byte challenge
   *
   * <p>3-byte transaction number
   *
   * <p>5 or 10 byte signature (hi part)
   *
   * @param undebitComplementaryData the data out from the SvPrepareDebit SAM command.
   * @since 2.0
   */
  public void finalizeBuilder(byte[] undebitComplementaryData) {
    if ((poRevision == PoRevision.REV3_2 && undebitComplementaryData.length != 20)
        || (poRevision != PoRevision.REV3_2 && undebitComplementaryData.length != 15)) {
      throw new IllegalArgumentException("Bad SV prepare load data length.");
    }

    byte p1 = undebitComplementaryData[4];
    byte p2 = undebitComplementaryData[5];

    dataIn[0] = undebitComplementaryData[6];
    System.arraycopy(undebitComplementaryData, 0, dataIn, 8, 4);
    System.arraycopy(undebitComplementaryData, 7, dataIn, 12, 3);
    System.arraycopy(
        undebitComplementaryData, 10, dataIn, 15, undebitComplementaryData.length - 10);

    setApduRequest(
        new ApduRequest(poClass.getValue(), command.getInstructionByte(), p1, p2, dataIn, null));
  }

  /**
   * Gets the SV Debit part of the data to include in the SAM SV Prepare Debit command
   *
   * @return a byte array containing the SV undebit data
   * @since 2.0
   */
  public byte[] getSvUndebitData() {
    byte[] svUndebitData = new byte[12];
    svUndebitData[0] = command.getInstructionByte();
    // svUndebitData[1,2] / P1P2 not set because ignored
    // Lc is 5 bytes longer in revision 3.2
    svUndebitData[3] = poRevision == PoRevision.REV3_2 ? (byte) 0x19 : (byte) 0x14;
    // appends the fixed part of dataIn
    System.arraycopy(dataIn, 0, svUndebitData, 4, 8);
    return svUndebitData;
  }

  /** {@inheritDoc} 9çào */
  @Override
  public PoSvUndebitParser createResponseParser(ApduResponse apduResponse) {
    return new PoSvUndebitParser(apduResponse, this);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This command modified the contents of the PO and therefore uses the session buffer.
   *
   * @return true
   * @since 2.0
   */
  @Override
  public boolean isSessionBufferUsed() {
    return true;
  }
}
