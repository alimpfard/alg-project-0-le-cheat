# Algorithms Project 0
### {list-all-group-members}
###### "_For the love of all that is holy, why even bring flower images up?_"-Edition

#### Abstract
##### The problem
Transformation of a sequence to another with a minimum number of actions.

##### Proposed solution
(iteratively) Find shared sublists of biggest length and adjust the members before the aforementioned list to match.


#### Details
Solution will be detailed in three pieces:

- piece 0 : finding the biggest shared sublist

- piece 1 : matching the elements before the shared sublist

- piece 2 : matching the elements after the shared sublist

##### Subproblem 0
__Initialization of the two lists:__
First and foremost, we may simply skip the already matching consecutive elements:
```haskell
unzip.dropWhile (\(x, y) -> x == y) (zip list0 list1)
```

##### Subproblem 1
__Finding the initial biggest sublist:__
iteratively (or recursively) we shall find the biggest shared sublist between the two lists by finding one shared element and linearly probing down.

##### Subproblem 2
__Matching the elements before the shared sublist:__

There are three possible ways for the sublist to appear:

+ beginning in $List_0$ = beginning in $List_1$
In which case, we may simply move on.

+ beginning in $List_0$ < beginning in $List_1$
In which case, we shall insert as many as $(b_{List_1} - b_{List_0})$ elements into $List_0$

+ beginning in $List_0$ > beginning in $List_1$
In which case, we shall delete as many as $(b_{List_0} - b_{List_1})$ elements from $List_0$


##### Subproblem 3
__Matching the elements after the shared sublist:__

we shall repeat from Subproblem 1 until we can no longer find any shared sublists;
In which case we will have three possible layouts:

+ none of $List_0$ or $List_1$ are empty
In this case we will linearly replace elements until one list runs out

+ $List_0$ has length 0, and $List_1$ has length > 0
Wherein we may simply insert the rest of the elements into $List_0$

+ $List_0$ has length > 0, and $List_1$ has length 0
Wherein we may simply discard the extra elements

#### Direct answer to the question, since people are always impatient

We may either use a recursive algorithm to traverse down the lists, or simply use dynamic methods. (see above for reasons!)
