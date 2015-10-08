# GCOM_middleware
Middleware for GCOM

# To implement
- Group management [ ]
- Communication protocols [ ]
- Message ordering [ ]

## Group Management
GCom needs to dynamicly track the members of the group
Dynamic: May join and leave whenever.
Re-join = New join


###TODO:
- Interface for :
  - Create Group [X]
  - Remove Group [ ]
  - Add member in group[X]
  - Remove member in group [X]
- Error detection
  - Monitor group [ ]
  - Detect crashes [ ]
- Change membership
  - Notify all members of change [ ]
- Resolve name [X]
  - Propagate name -> list of members. [X]

### DONE:

## Communication protocol
Group creator specifies communication methods. For that group.

### TODO:
- Non-reliable multicast [X]
- Reliable multicast [ ] (Optional)
- Tree-based reliable multicast [ ] (Optional)
  - Reliable [ ]
  - Children and parent [ ]
  - Plan [ ]
  - Presented [ ]

### DONE:

## Message ordering
Messages must be shown in order but must not arrive in that order.
### TODO:
- FIFO [ ] (Optional)
- Causal [ ] 
- Total [ ] (Optional)
- Causal-Total [ ] (Optional)
- Unordered [X]
