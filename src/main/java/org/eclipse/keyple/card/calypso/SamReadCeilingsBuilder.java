/* **************************************************************************************
 * Copyright (c) 2019 Calypso Networks Association https://www.calypsonet-asso.org/
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

import org.eclipse.keyple.card.calypso.sam.SamRevision;
import org.eclipse.keyple.core.card.ApduRequest;
import org.eclipse.keyple.core.card.ApduResponse;

/**
 * (package-private) <br>
 * Builds the Read Ceilings APDU command.
 *
 * @since 2.0
 */
final class SamReadCeilingsBuilder extends AbstractSamCommandBuilder<SamReadCeilingsParser> {
  /** The command reference. */
  private static final SamCommand command = SamCommand.READ_CEILINGS;

  public static final int MAX_CEILING_NUMB = 26;

  public static final int MAX_CEILING_REC_NUMB = 3;

  /** Ceiling operation type */
  public enum CeilingsOperationType {
    /** Ceiling record */
    CEILING_RECORD,
    /** Single ceiling */
    SINGLE_CEILING
  }

  /**
   * Instantiates a new SamReadCeilingsBuilder.
   *
   * @param revision revision of the SAM.
   * @param operationType the counter operation type.
   * @param index the counter index.
   * @since 2.0
   */
  public SamReadCeilingsBuilder(
      SamRevision revision, CeilingsOperationType operationType, int index) {

    super(command);
    if (revision != null) {
      this.defaultRevision = revision;
    }

    byte cla = this.defaultRevision.getClassByte();

    byte p1;
    byte p2;

    if (operationType == CeilingsOperationType.CEILING_RECORD) {
      if (index < 0 || index > MAX_CEILING_REC_NUMB) {
        throw new IllegalArgumentException(
            "Record Number must be between 1 and " + MAX_CEILING_REC_NUMB + ".");
      }
      p1 = (byte) 0x00;
      p2 = (byte) (0xB0 + index);
    } else {
      // SINGLE_CEILING:

      if (index < 0 || index > MAX_CEILING_NUMB) {
        throw new IllegalArgumentException(
            "Counter Number must be between 0 and " + MAX_CEILING_NUMB + ".");
      }
      p1 = (byte) index;
      p2 = (byte) (0xB8);
    }

    setApduRequest(new ApduRequest(cla, command.getInstructionByte(), p1, p2, null, (byte) 0x00));
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public SamReadCeilingsParser createResponseParser(ApduResponse apduResponse) {
    return new SamReadCeilingsParser(apduResponse, this);
  }
}
