/* **************************************************************************************
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
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

import java.util.HashMap;
import java.util.Map;
import org.eclipse.keyple.core.card.ApduResponse;

/**
 * Parses the Digest close response.
 *
 * @since 2.0
 */
final class SamDigestCloseParser extends AbstractSamResponseParser {

  private static final Map<Integer, StatusProperties> STATUS_TABLE;

  static {
    Map<Integer, StatusProperties> m =
        new HashMap<Integer, StatusProperties>(AbstractSamResponseParser.STATUS_TABLE);
    m.put(
        0x6985,
        new StatusProperties(
            "Preconditions not satisfied.", CalypsoSamAccessForbiddenException.class));
    STATUS_TABLE = m;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  protected Map<Integer, StatusProperties> getStatusTable() {
    return STATUS_TABLE;
  }

  /**
   * Instantiates a new SamDigestCloseParser.
   *
   * @param response from the SamDigestCloseBuilder.
   * @param builder the reference to the builder that created this parser.
   * @since 2.0
   */
  public SamDigestCloseParser(ApduResponse response, SamDigestCloseBuilder builder) {
    super(response, builder);
  }

  /**
   * Gets the sam signature.
   *
   * @return the sam half session signature
   * @since 2.0
   */
  public byte[] getSignature() {
    return isSuccessful() ? response.getDataOut() : null;
  }
}
