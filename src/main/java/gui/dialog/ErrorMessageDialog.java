package gui.dialog;

import messages.ErrorMessages;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Created by Nikola on 22.6.2015..
 */
public class ErrorMessageDialog {

    public static void show(Window window, Exception exception) {
        String errorMessage;

        if (exception instanceof SQLException) {
            int errorCode = ((SQLException) exception).getErrorCode();

            if (errorCode == ErrorMessages.CUSTOM_CODE) {
                errorMessage = exception.getMessage();
            } else if (errorCode == 547) {
                errorMessage = "Slog ne može biti obrisan jer jer referenciran u drugoj tabeli.";
            } else {
                errorMessage = exception.getMessage();
            }
        } else {
            errorMessage = exception.getMessage();
        }

        JOptionPane.showMessageDialog(window, errorMessage, ErrorMessages.TITLE,
                JOptionPane.ERROR_MESSAGE);

    }

    private ErrorMessageDialog() {
    }
}
