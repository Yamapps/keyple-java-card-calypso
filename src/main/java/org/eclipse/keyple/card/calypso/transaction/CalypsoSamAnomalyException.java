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
package org.eclipse.keyple.card.calypso.transaction;

/**
 * Indicates an anomaly in the SAM.<br>
 * This can occur if the SAM is not Calypso compliant.
 */
public class CalypsoSamAnomalyException extends CalypsoPoTransactionException {

  /**
   * @param message message to identify the exception context.
   * @param cause the cause.
   */
  public CalypsoSamAnomalyException(String message, Throwable cause) {
    super(message, cause);
  }
}
