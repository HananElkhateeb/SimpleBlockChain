package com.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class DatasetGenerator {
    private final int NUM_OF_CLIENTS = 7;
    private final int NUM_OF_TRANSACTIONS = 1600;
    private final int NUM_OF_MINERS = 3;
    private final int NUM_OF_OUTPUTS_PER_TRANSACTIONS = 3;

    private final int MIN_VALUE = 20;
    private final int MAX_VALUE = 100;

    private int numOfTransactions = 1;
    private HashMap<Integer, ArrayList<Integer>> previousTransactions;

    public DatasetGenerator() {
        previousTransactions = new HashMap<>();
    }

    public static void main(String[] args) {
        String fileContent = "";
        DatasetGenerator datasetGenerator = new DatasetGenerator();
        fileContent += datasetGenerator.generateInitializationTransactions();
        fileContent += datasetGenerator.generateTransactions();
        datasetGenerator.writeToFile(fileContent);
    }

    private String generateTransactions() {
        DecimalFormat df = new DecimalFormat("##.#####");
        String transactions = "";
        while(numOfTransactions <= NUM_OF_TRANSACTIONS) {
            String transaction = "";
            int randomInput = ThreadLocalRandom.current().nextInt(1, NUM_OF_CLIENTS + 1);
            int randomPrevTxIndex = ThreadLocalRandom.current().nextInt(0, previousTransactions.get(randomInput).size());
            int randomPrevTx = previousTransactions.get(randomInput).get(randomPrevTxIndex);
            int randomOutputIndex = ThreadLocalRandom.current().nextInt(1, NUM_OF_MINERS + 1);
            transaction += numOfTransactions + "\t" + "input:" + randomInput + "\t" + "previoustx:" + randomPrevTx +
                    "\t" + "outputindex:" + randomOutputIndex + "\t";
            for (int i = 1; i <= NUM_OF_OUTPUTS_PER_TRANSACTIONS; i++) {
                String randomValue = df.format(Math.random() * (10));
                int randomClient = ThreadLocalRandom.current().nextInt(1, NUM_OF_CLIENTS + 1);
                transaction += "value" + i + ":" + randomValue + "\t" + "output" + i + ":" + randomClient + "\t";
                previousTransactions.get(randomClient).add(numOfTransactions);
            }
            numOfTransactions++;
            transactions += transaction +"\n";
        }
        return transactions;
    }

    private String generateInitializationTransactions() {
        String initialTransactions = "";
        DecimalFormat df = new DecimalFormat("##.#####");
        for(int i = 1; i <= NUM_OF_CLIENTS; i++) {
            String random = df.format(MIN_VALUE + Math.random() * (MAX_VALUE - MIN_VALUE));
            String initTrans =
                    numOfTransactions + "\t" + "input:0" + "\t" + "value:" + random + "\t" + "output:" + i + "\n";
            previousTransactions.put(i, new ArrayList<>());
            previousTransactions.get(i).add(numOfTransactions);
            numOfTransactions++;
            initialTransactions += initTrans;
        }
        return initialTransactions;
    }

    private void writeToFile(String fileContent) {
        String fileName = "dataset_" + NUM_OF_CLIENTS +"Clients_" + NUM_OF_TRANSACTIONS + "Transactions.txt";
        String workspacePath = "/Users/islam/IdeaProjects/SimpleBlockChain/src/com/resources";
        File file = new File(workspacePath + File.separator  + fileName);
        FileWriter fw;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            BufferedWriter WriteFileBuffer = new BufferedWriter(fw);
            WriteFileBuffer.write(fileContent);
            WriteFileBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
