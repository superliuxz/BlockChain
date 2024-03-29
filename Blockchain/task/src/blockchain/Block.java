package blockchain;

import blockchain.utils.MessageUtil;
import blockchain.utils.StringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Block implements Serializable {

  private Integer id;
  private Long creationTimestamp;
  private String prevHash;
  private String hash;
  private Long computeTime;
  private Integer magicNumber;
  private String minerId;
  private Integer numZeros;
  private String numZeroChanges;
  private ArrayList<Message> message;

  public Block(int blockId, String minerId, String prevHash, int numZeros) {
    this.creationTimestamp = new Date().getTime();
    this.id = blockId;
    this.minerId = minerId;
    this.prevHash = prevHash;
    this.numZeros = numZeros;
  }

  public void calcHash(ArrayList<Message> message) {
    this.message = message;
    this.magicNumber = new Random().nextInt() & Integer.MAX_VALUE;
    this.hash = StringUtil.applySha256(
        this.prevHash +
            this.id.toString() +
            this.creationTimestamp.toString() +
            this.magicNumber.toString() +
            MessageUtil.joinMessages(this.message));
    this.computeTime = (new Date().getTime() - this.creationTimestamp) / 1000;
  }

  public boolean validHash() {
    return this.hash != null && this.hash.startsWith("0".repeat(this.numZeros));
  }

  public String getHash() {
    return this.hash;
  }

  public String getPrevHash() {
    return this.prevHash;
  }

  public Long getComputeTime() {
    return this.computeTime;
  }

  public synchronized void setNumZeroChanges(String change) {
    this.numZeroChanges = change;
  }

  public synchronized Integer getMessageSize() {
    return this.message.size();
  }

  public ArrayList<Message> getMessage() {
    return this.message;
  }

  @Override
  public String toString() {
    return String.format(
        "Block:\n"
            + "Created by miner # %s\n"
            + "Id: %s\n"
            + "Timestamp: %s\n"
            + "Magic number: %s\n"
            + "Hash of the previous block:\n%s\n"
            + "Hash of the block:\n%s\n"
            + "Block data:\n%s\n"
            + "Block was generating for %s seconds\n"
            + "%s\n",
        this.minerId,
        this.id,
        this.creationTimestamp.toString(),
        this.magicNumber,
        this.prevHash,
        this.hash,
        MessageUtil.joinMessages(this.message),
        this.computeTime,
        this.numZeroChanges
    );
  }
}
