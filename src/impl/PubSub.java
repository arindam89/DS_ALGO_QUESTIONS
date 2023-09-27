package impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Question:
 * Implement a simple Publisher Subscriber program in Java where we have channels storing messages. Each channel can have any number of publishers and any number of subscribers. Subscribers provide a callback function that talks about what to do when a message gets published to the channel.  Overall, Publishers should be able to publish to channels, and subscribers for a channel should be able to receive the messages and call the callback on them. Additionally, subscribers can replay from index from a channel if it crashes for some reason. While replaying messages from an offset, subscribers will not receive live messages like other subscribers. A subscriber needs to reset itself to get its usual way of getting messages.
 *
 *
 */

class Message {
  private final int index;
  private final String content;

  public Message(int index, String content) {
    this.index = index;
    this.content = content;
  }

  public int getIndex() {
    return index;
  }

  public String getContent() {
    return content;
  }
}

class Channel {
  private final List<Message> messages;
  private final List<Subscriber> subscribers;

  public Channel() {
    this.messages = new ArrayList<>();
    this.subscribers = new ArrayList<>();
  }

  public void subscribe(Subscriber subscriber) {
    subscribers.add(subscriber);
    // Send existing messages to the new subscriber
    for (Message message : messages) {
      subscriber.receiveMessage(message);
    }
  }

  public void publishMessage(String content) {
    int newIndex = messages.size();
    Message message = new Message(newIndex, content);
    messages.add(message);

    for (Subscriber subscriber : subscribers) {
      subscriber.receiveMessage(message);
    }
  }

  public void replayFromIndex(int startIndex, Subscriber subscriber) {
    for (int i = startIndex; i < messages.size(); i++) {
      subscriber.receiveMessage(messages.get(i));
    }
  }
}

class Publisher {
  private final Channel channel;

  public Publisher(Channel channel) {
    this.channel = channel;
  }

  public void publish(String content) {
    channel.publishMessage(content);
  }
}

class Subscriber {
  private final Channel channel;
  private final AtomicInteger replayIndex;
  private boolean isReplaying;

  public Subscriber(Channel channel) {
    this.channel = channel;
    this.replayIndex = new AtomicInteger(0);
    this.isReplaying = false;
    channel.subscribe(this);
  }

  public void receiveMessage(Message message) {
    if (!isReplaying) {
      System.out.println("Received message: " + message.getContent());
    }
  }

  public void startReplayFromIndex(int startIndex) {
    this.isReplaying = true;
    replayIndex.set(startIndex);
    channel.replayFromIndex(startIndex, this);
  }

  public void stopReplay() {
    this.isReplaying = false;
  }
}

public class PubSub {
  public static void main(String[] args) {
    Channel channel = new Channel();
    Publisher publisher1 = new Publisher(channel);
    Publisher publisher2 = new Publisher(channel);
    Subscriber subscriber1 = new Subscriber(channel);
    Subscriber subscriber2 = new Subscriber(channel);

    publisher1.publish("Message 1");
    publisher2.publish("Message 2");
    subscriber1.startReplayFromIndex(0);
    publisher1.publish("Message 3");
    subscriber2.startReplayFromIndex(1);
    publisher2.publish("Message 4");
    subscriber1.stopReplay();
    publisher1.publish("Message 5");
  }
}

/*
 * Footnote:
 * In the provided program, the `AtomicInteger` is used for the `replayIndex` to
 * ensure thread safety when multiple subscribers are accessing and updating the
 * `replayIndex` concurrently. Here's why it's necessary:
 * 
 * 1. **Concurrent Access**: In a real-world scenario, multiple subscribers may
 * be accessing the `replayIndex` concurrently. Without proper synchronization,
 * concurrent access to a simple integer variable can lead to data races and
 * unpredictable behavior. By using `AtomicInteger`, you ensure that the
 * operations on the `replayIndex` are atomic, meaning they are executed without
 * interruption.
 * 
 * 2. **Atomic Operations**: The `AtomicInteger` class provides atomic
 * operations like `get()` and `set()`, which are performed in a single, atomic
 * step. This means that even if multiple threads try to read or update the
 * `replayIndex` simultaneously, there won't be any race conditions or
 * inconsistencies in its value.
 * 
 * 3. **Consistent Replay Index**: Since subscribers may start and stop
 * replaying messages at different times, it's crucial to maintain a consistent
 * `replayIndex` for each subscriber. Using `AtomicInteger` ensures that when a
 * subscriber starts or stops replaying, the index is updated atomically,
 * preventing any interleaving of operations by different threads.
 * 
 * Here's an example of how `AtomicInteger` helps with concurrent access:
 * 
 * ```java
 * // Without AtomicInteger (not thread-safe)
 * int replayIndex = 0;
 * // ...
 * 
 * // Thread 1
 * replayIndex = replayIndex + 1;
 * 
 * // Thread 2
 * replayIndex = replayIndex + 1;
 * ```
 * 
 * In the above code, if Thread 1 and Thread 2 both access and update
 * `replayIndex` concurrently, there's a risk of them both reading the same
 * value and then overwriting each other's changes, leading to incorrect
 * results.
 * 
 * On the other hand, when using `AtomicInteger`:
 * 
 * ```java
 * // With AtomicInteger (thread-safe)
 * AtomicInteger replayIndex = new AtomicInteger(0);
 * // ...
 * 
 * // Thread 1
 * replayIndex.incrementAndGet(); // Atomically increment by 1
 * 
 * // Thread 2
 * replayIndex.incrementAndGet(); // Atomically increment by 1
 * ```
 * 
 * With `AtomicInteger`, the `incrementAndGet()` operation ensures that only one
 * thread at a time can modify the value of `replayIndex`, preventing conflicts
 * and ensuring that the index is updated correctly in a thread-safe manner.
 * This is why it's a good choice for managing the `replayIndex` in a
 * multi-threaded environment.
 */
