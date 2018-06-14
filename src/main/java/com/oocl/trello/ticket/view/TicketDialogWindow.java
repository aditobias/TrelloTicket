package com.oocl.trello.ticket.view;

import javax.swing.*;
import java.awt.event.*;

public class TicketDialogWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField numberTextField;
    private JTextField severityTextField;
    private JTextField categoryTextField;
    private JTextField subjectTextField;
    private JTextField statusTextField;
    private JTextField responsiblePartyTextField;
    private JTextField creationDateTextField;
    private JTextField linkTextField;
    private JLabel labelNumber;
    private JLabel labelSeverity;
    private JLabel labelCategory;
    private JLabel labelSubject;
    private JLabel labelStatus;
    private JLabel labelRespParty;
    private JLabel labelCreationDate;
    private JLabel labelLink;


    public TicketDialogWindow() {
        setContentPane(contentPane);
        setTitle("Ticket Details");
        pack();
        getRootPane().setDefaultButton(buttonOK);
        setSize(650,300);
        setVisible(true);
    }

    @Override
    public JPanel getContentPane() {
        return contentPane;
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    public void setButtonOK(JButton buttonOK) {
        this.buttonOK = buttonOK;
    }

    public JButton getButtonCancel() {
        return buttonCancel;
    }

    public void setButtonCancel(JButton buttonCancel) {
        this.buttonCancel = buttonCancel;
    }

    public JTextField getNumberTextField() {
        return numberTextField;
    }

    public void setNumberTextField(JTextField numberTextField) {
        this.numberTextField = numberTextField;
    }

    public JTextField getSeverityTextField() {
        return severityTextField;
    }

    public void setSeverityTextField(JTextField severityTextField) {
        this.severityTextField = severityTextField;
    }

    public JTextField getCategoryTextField() {
        return categoryTextField;
    }

    public void setCategoryTextField(JTextField categoryTextField) {
        this.categoryTextField = categoryTextField;
    }

    public JTextField getSubjectTextField() {
        return subjectTextField;
    }

    public void setSubjectTextField(JTextField subjectTextField) {
        this.subjectTextField = subjectTextField;
    }

    public JTextField getStatusTextField() {
        return statusTextField;
    }

    public void setStatusTextField(JTextField statusTextField) {
        this.statusTextField = statusTextField;
    }

    public JTextField getResponsiblePartyTextField() {
        return responsiblePartyTextField;
    }

    public void setResponsiblePartyTextField(JTextField responsiblePartyTextField) {
        this.responsiblePartyTextField = responsiblePartyTextField;
    }

    public JTextField getCreationDateTextField() {
        return creationDateTextField;
    }

    public void setCreationDateTextField(JTextField creationDateTextField) {
        this.creationDateTextField = creationDateTextField;
    }

    public JTextField getLinkTextField() {
        return linkTextField;
    }

    public void setLinkTextField(JTextField linkTextField) {
        this.linkTextField = linkTextField;
    }

    public JLabel getLabelNumber() {
        return labelNumber;
    }

    public void setLabelNumber(JLabel labelNumber) {
        this.labelNumber = labelNumber;
    }

    public JLabel getLabelSeverity() {
        return labelSeverity;
    }

    public void setLabelSeverity(JLabel labelSeverity) {
        this.labelSeverity = labelSeverity;
    }

    public JLabel getLabelCategory() {
        return labelCategory;
    }

    public void setLabelCategory(JLabel labelCategory) {
        this.labelCategory = labelCategory;
    }

    public JLabel getLabelSubject() {
        return labelSubject;
    }

    public void setLabelSubject(JLabel labelSubject) {
        this.labelSubject = labelSubject;
    }

    public JLabel getLabelStatus() {
        return labelStatus;
    }

    public void setLabelStatus(JLabel labelStatus) {
        this.labelStatus = labelStatus;
    }

    public JLabel getLabelRespParty() {
        return labelRespParty;
    }

    public void setLabelRespParty(JLabel labelRespParty) {
        this.labelRespParty = labelRespParty;
    }

    public JLabel getLabelCreationDate() {
        return labelCreationDate;
    }

    public void setLabelCreationDate(JLabel labelCreationDate) {
        this.labelCreationDate = labelCreationDate;
    }

    public JLabel getLabelLink() {
        return labelLink;
    }

    public void setLabelLink(JLabel labelLink) {
        this.labelLink = labelLink;
    }






}
