## Search Documents

### Problem Statement

You are given a list of documents from some data source. Each document is a text content blob containing one or more words. The document format can look something like this.

```
type Document = {
    docId: <uuid>,
    content: <string>
}

type Data = Document[]
```
* You are given the task of implementing a `search()` functionality which takes a word as input and returns all documents where the given word can be found.
* You need to extend this solution to search for a phrase as an input and return all documents where the exact phrase can be found.

### Example

```
Input:

Data = [
    {
        docId: 1,
        content: "Hello World. I am listening.",
    },
    {
        docId: 5,
        content: "World, is round",
    },
    {
        docId: 8,
        content: "Hi, Hello! Can you hear me?",
    },
]

Output:

search("Hello") => [1,8]
search("World") => [1,5]
search("hear") => [8]
search("There") => []

Extention:
search("Hello World") => [1]
search("Hello! Can") => [5]

```