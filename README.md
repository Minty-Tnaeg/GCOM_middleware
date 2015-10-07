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
- Interface for:
  - Create [X]
  - Remove [ ]
  - Add member [X]
  - Remove member [ ]
- Error detection
  - Monitor group [ ]
  - Detect crashes [ ]
- Change membership
  - Notify all members of change [ ]
- Resolve name [ ]
  - Propagate name -> list of members. [ ]

### DONE:

## Communication protocol
Group creator specifies communication methods. For that group.

### TODO:
- Non-reliable multicast [X]
- Reliable multicast [ ]
- Tree-based reliable multicast [ ]
  - Reliable [ ]
  - Children and parent [ ]
  - Plan [ ]
  - Presented [ ]

### DONE:

## Message ordering
Messages must be shown in order but must not arrive in that order.
### TODO:
- FIFO [ ]
- Causal [ ]
- Total [ ]
- Causal-Total [ ]
- Unordered [X]
