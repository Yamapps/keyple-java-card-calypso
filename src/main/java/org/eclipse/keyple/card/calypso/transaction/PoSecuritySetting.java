/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://www.calypsonet-asso.org/
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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.eclipse.keyple.core.util.Assert;

/**
 * This POJO contains all the needed data to manage the security operations of a Calypso
 * transaction.
 *
 * <p>Fluent setters allow to define all the required parameters, among which the resource profile
 * of the SAM card is the only mandatory one.
 *
 * @since 2.0
 */
public class PoSecuritySetting {

  private final String samCardResourceProfileName;
  private final boolean isMultipleSessionEnabled;
  private final boolean isRatificationMechanismEnabled;
  private final boolean isPinTransmissionEncryptionDisabled;
  private final EnumMap<PoTransactionService.SessionAccessLevel, Byte> kifBySessionLevel;
  private final EnumMap<PoTransactionService.SessionAccessLevel, Byte> kvcBySessionLevel;
  private final EnumMap<PoTransactionService.SessionAccessLevel, Byte>
      keyRecordNumberBySessionLevel;
  private final List<Byte> authorizedKvcList;
  private final byte pinCipheringKif;
  private final byte pinCipheringKvc;
  private final boolean isLoadAndDebitSvLogRequired;
  private final boolean isSvNegativeBalanceAllowed;

  /**
   * (private)
   *
   * @param builder The {@link PoSecuritySettingBuilder}.
   */
  private PoSecuritySetting(PoSecuritySettingBuilder builder) {
    this.samCardResourceProfileName = builder.samCardResourceProfileName;
    this.isMultipleSessionEnabled = builder.isMultipleSessionEnabled;
    this.isRatificationMechanismEnabled = builder.isRatificationMechanismEnabled;
    this.isPinTransmissionEncryptionDisabled = builder.isPinTransmissionEncryptionDisabled;
    this.kifBySessionLevel = builder.kifBySessionLevel;
    this.kvcBySessionLevel = builder.kvcBySessionLevel;
    this.keyRecordNumberBySessionLevel = builder.keyRecordNumberBySessionLevel;
    this.authorizedKvcList = builder.authorizedKvcList;
    this.pinCipheringKif = builder.pinCipheringKif;
    this.pinCipheringKvc = builder.pinCipheringKvc;
    this.isLoadAndDebitSvLogRequired = builder.isLoadAndDebitSvLogRequired;
    this.isSvNegativeBalanceAllowed = builder.isSvNegativeBalanceAllowed;
  }

  /**
   * Gets the SAM card resource profile name.
   *
   * @return A not empty string.
   * @since 2.0
   */
  public String getCardResourceProfileName() {
    return samCardResourceProfileName;
  }

  /**
   * Tells if the multiple session mode is enabled.
   *
   * <p>The default value is {@code false}.
   *
   * @return A not null reference.
   * @since 2.0
   */
  public boolean isMultipleSessionEnabled() {
    return isMultipleSessionEnabled;
  }

  /**
   * Tells if the ratification mechanism is enabled.
   *
   * <p>The default value is {@code false}.
   *
   * @return A not null reference.
   * @since 2.0
   */
  public boolean isRatificationMechanismEnabled() {
    return isRatificationMechanismEnabled;
  }

  /**
   * Tells if the encryption of the PIN transmission is disabled.
   *
   * <p>The default value is {@code false}.
   *
   * @return A not null reference.
   * @since 2.0
   */
  public boolean isPinTransmissionEncryptionDisabled() {
    return isPinTransmissionEncryptionDisabled;
  }

  /**
   * Gets the default session KIF for the provided session level.
   *
   * <p>TODO check what default values should be used here.
   *
   * @param sessionAccessLevel The session level.
   * @return null if no value has been set.
   * @since 2.0
   */
  public Byte getKif(PoTransactionService.SessionAccessLevel sessionAccessLevel) {
    return kifBySessionLevel.get(sessionAccessLevel);
  }

  /**
   * Gets the default session KVC for the provided session level.
   *
   * <p>TODO check what default values should be used here.
   *
   * @param sessionAccessLevel The session level.
   * @return null if no value has been set.
   * @since 2.0
   */
  public Byte getKvc(PoTransactionService.SessionAccessLevel sessionAccessLevel) {
    return kvcBySessionLevel.get(sessionAccessLevel);
  }

  /**
   * Gets the default session key record number for the provided session level.
   *
   * <p>TODO check what default values should be used here.
   *
   * @param sessionAccessLevel The session level.
   * @return null if no value has been set.
   * @since 2.0
   */
  public Byte getKeyRecordNumber(PoTransactionService.SessionAccessLevel sessionAccessLevel) {
    return keyRecordNumberBySessionLevel.get(sessionAccessLevel);
  }

  /**
   * Check if the provided KVC value is authorized or not.
   *
   * <p>If no list of authorized kvc is defined (authorizedKvcList empty), the method returns true
   * regardless of the value provided.
   *
   * @param kvc The KVC value to be check.
   * @return true if the kvc is authorized or if no authorization list has been defined.
   * @since 2.0
   */
  public boolean isKvcAuthorized(byte kvc) {
    return authorizedKvcList.isEmpty() || authorizedKvcList.contains(kvc);
  }

  /**
   * Gets the default KIF to be used for PIN encryption.
   *
   * <p>The default value is 0.
   *
   * @return A byte.
   * @since 2.0
   */
  public byte getPinCipheringKif() {
    return pinCipheringKif;
  }

  /**
   * Gets the default KVC to be used for PIN encryption.
   *
   * <p>The default value is 0.
   *
   * @return A byte.
   * @since 2.0
   */
  public byte getPinCipheringKvc() {
    return pinCipheringKvc;
  }

  /**
   * Indicates whether both the debit and load logs must be retrieved during SV operations.
   *
   * <p>The default value is false.
   *
   * @return true if both logs are required, false if not.
   * @since 2.0
   */
  public boolean isLoadAndDebitSvLogRequired() {
    return isLoadAndDebitSvLogRequired;
  }

  /**
   * Indicates whether negative balances are allowed in SV transactions.
   *
   * <p>The default value is false.
   *
   * @return true if negative balances are allowed, false if not.
   * @since 2.0
   */
  public boolean isSvNegativeBalanceAllowed() {
    return isSvNegativeBalanceAllowed;
  }

  /**
   * Creates an instance of {@link PoSecuritySetting} builder to setup the security options for the
   * {@link PoTransactionService}.
   *
   * <p>The only mandatory parameter is the name of the SAM card resource.
   *
   * <p>The default values of the other parameters are documented in their respective getters.
   *
   * @param samCardResourceProfileName The name of the SAM card resource associated with these
   *     parameters.
   * @return A builder instance.
   * @since 2.0
   */
  public static PoSecuritySettingBuilder builder(String samCardResourceProfileName) {
    return new PoSecuritySettingBuilder(samCardResourceProfileName);
  }

  /**
   * Builder of {@link PoSecuritySetting}.
   *
   * @since 2.0
   */
  public static class PoSecuritySettingBuilder {

    private final String samCardResourceProfileName;
    private boolean isMultipleSessionEnabled;
    private boolean isRatificationMechanismEnabled;
    private boolean isPinTransmissionEncryptionDisabled;
    private final EnumMap<PoTransactionService.SessionAccessLevel, Byte> kifBySessionLevel;
    private final EnumMap<PoTransactionService.SessionAccessLevel, Byte> kvcBySessionLevel;
    private final EnumMap<PoTransactionService.SessionAccessLevel, Byte>
        keyRecordNumberBySessionLevel;
    private List<Byte> authorizedKvcList;
    private byte pinCipheringKif;
    private byte pinCipheringKvc;
    private boolean isLoadAndDebitSvLogRequired;
    private boolean isSvNegativeBalanceAllowed;

    /**
     * Creates an instance of {@link PoSecuritySetting} to setup the security options for the {@link
     * PoTransactionService}.
     *
     * <p>The only mandatory parameter is the name of the SAM card resource.
     *
     * <p>The default values of the other parameters are documented in their respective getters.
     *
     * @param samCardResourceProfileName The name of the SAM card resource associated with these
     *     parameters.
     */
    private PoSecuritySettingBuilder(String samCardResourceProfileName) {
      this.samCardResourceProfileName = samCardResourceProfileName;
      // set default values for all optional parameters
      this.isMultipleSessionEnabled = false;
      this.isRatificationMechanismEnabled = false;
      this.isPinTransmissionEncryptionDisabled = false;
      this.kifBySessionLevel =
          new EnumMap<PoTransactionService.SessionAccessLevel, Byte>(
              PoTransactionService.SessionAccessLevel.class);
      this.kvcBySessionLevel =
          new EnumMap<PoTransactionService.SessionAccessLevel, Byte>(
              PoTransactionService.SessionAccessLevel.class);
      this.keyRecordNumberBySessionLevel =
          new EnumMap<PoTransactionService.SessionAccessLevel, Byte>(
              PoTransactionService.SessionAccessLevel.class);
      this.authorizedKvcList = new ArrayList<Byte>();
      this.pinCipheringKif = (byte) 0;
      this.pinCipheringKvc = (byte) 0;
      this.isLoadAndDebitSvLogRequired = false;
      this.isSvNegativeBalanceAllowed = false;
    }

    /**
     * Enable multiple session mode to allow more changes to the card than the session buffer can
     * handle.
     *
     * @return The object instance.
     * @throws IllegalArgumentException If the argument is null.
     * @since 2.0
     */
    public PoSecuritySettingBuilder enableMultipleSession() {
      this.isMultipleSessionEnabled = true;
      return this;
    }

    /**
     * Enable the ratification mechanism to handle the early removal of the card preventing the
     * terminal from receiving the acknowledgement of the session closing.
     *
     * <p>This feature is particularly useful for validators.
     *
     * @return The object instance.
     * @throws IllegalArgumentException If the argument is null.
     * @since 2.0
     */
    public PoSecuritySettingBuilder enableRatificationMechanism() {
      this.isRatificationMechanismEnabled = true;
      return this;
    }

    /**
     * Disable the PIN transmission encryption.
     *
     * @return The object instance.
     * @throws IllegalArgumentException If the argument is null.
     * @since 2.0
     */
    public PoSecuritySettingBuilder disablePinEncryption() {
      this.isPinTransmissionEncryptionDisabled = true;
      return this;
    }

    /**
     * Set the default KIF for the provide session level.
     *
     * @param sessionAccessLevel the session level.
     * @param kif the desired default KIF.
     * @return The object instance.
     * @throws IllegalArgumentException If sessionAccessLevel is null.
     * @since 2.0
     */
    public PoSecuritySettingBuilder assignKif(
        PoTransactionService.SessionAccessLevel sessionAccessLevel, byte kif) {
      Assert.getInstance().notNull(sessionAccessLevel, "sessionAccessLevel");
      this.kifBySessionLevel.put(sessionAccessLevel, kif);
      return this;
    }

    /**
     * Set the default KVC for the provide session level. P
     *
     * @param sessionAccessLevel the session level.
     * @param kvc the desired default KVC.
     * @return The object instance.
     * @throws IllegalArgumentException If sessionAccessLevel is null.
     * @since 2.0
     */
    public PoSecuritySettingBuilder assignKvc(
        PoTransactionService.SessionAccessLevel sessionAccessLevel, byte kvc) {
      Assert.getInstance().notNull(sessionAccessLevel, "sessionAccessLevel");
      this.kvcBySessionLevel.put(sessionAccessLevel, kvc);
      return this;
    }

    /**
     * Set the default key record number
     *
     * @param sessionAccessLevel the session level.
     * @param keyRecordNumber the desired default key record number.
     * @return The object instance.
     * @throws IllegalArgumentException If sessionAccessLevel is null.
     * @since 2.0
     */
    public PoSecuritySettingBuilder assignKeyRecordNumber(
        PoTransactionService.SessionAccessLevel sessionAccessLevel, byte keyRecordNumber) {
      Assert.getInstance().notNull(sessionAccessLevel, "sessionAccessLevel");
      this.keyRecordNumberBySessionLevel.put(sessionAccessLevel, keyRecordNumber);
      return this;
    }

    /**
     * Provides a list of authorized KVC
     *
     * @param sessionAuthorizedKvcList The list of authorized KVCs.
     * @return The object instance.
     * @throws IllegalArgumentException If sessionAuthorizedKvcList is null or empty.
     * @since 2.0
     */
    public PoSecuritySettingBuilder sessionAuthorizedKvcList(List<Byte> sessionAuthorizedKvcList) {
      Assert.getInstance().notEmpty(sessionAuthorizedKvcList, "sessionAuthorizedKvcList");
      this.authorizedKvcList = sessionAuthorizedKvcList;
      return this;
    }

    /**
     * Provides the KIF/KVC pair of the PIN ciphering key
     *
     * <p>The default value for both KIF and KVC is 0.
     *
     * @param kif the KIF of the PIN ciphering key.
     * @param kvc the KVC of the PIN ciphering key.
     * @return The object instance.
     * @since 2.0
     */
    public PoSecuritySettingBuilder pinCipheringKey(byte kif, byte kvc) {
      this.pinCipheringKif = kif;
      this.pinCipheringKvc = kvc;
      return this;
    }

    /**
     * Sets the SV Get log read mode.
     *
     * <p>The default value is false.
     *
     * @param isLoadAndDebitSvLogRequired true if both Load and Debit logs are required.
     * @return The object instance.
     * @since 2.0
     */
    public PoSecuritySettingBuilder isLoadAndDebitSvLogRequired(
        boolean isLoadAndDebitSvLogRequired) {
      this.isLoadAndDebitSvLogRequired = isLoadAndDebitSvLogRequired;
      return this;
    }

    /**
     * Sets the SV negative balance.
     *
     * <p>The default value is false.
     *
     * @param isSvNegativeBalanceAllowed true if negative balance is allowed, false if not.
     * @return The object instance.
     * @since 2.0
     */
    public PoSecuritySettingBuilder isSvNegativeBalanceAllowed(boolean isSvNegativeBalanceAllowed) {
      this.isSvNegativeBalanceAllowed = isSvNegativeBalanceAllowed;
      return this;
    }

    /**
     * Creates an instance of {@link PoSecuritySetting}.
     *
     * @return A not null reference.
     * @since 2.0
     */
    public PoSecuritySetting build() {
      return new PoSecuritySetting(this);
    }
  }
}
