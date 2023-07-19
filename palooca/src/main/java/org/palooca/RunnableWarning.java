/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.palooca;

import javax.swing.JOptionPane;

/**
 *
 * @author chris
 */
public class RunnableWarning implements Runnable {

        private final String message;
        private final String title;

        public RunnableWarning(String message, String title) {
            this.message = message;
            this.title = title;
        }

        @Override
        public void run() {
            Runnable worker = () -> JOptionPane.showMessageDialog(null,
                        message,
                        title, JOptionPane.WARNING_MESSAGE);
            javax.swing.SwingUtilities.invokeLater(worker);
            //OSUtil.invokeUI(worker);
        }
    }
