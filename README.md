# GCOM_middleware
Middleware for GCOM

# To implement
- Group management [ ]
- Communication protocols [ ]
- Message ordering [ ]

## Group Management
GCom needs to staticly or dynamicly track the members of the group

Static: Group pre-defined, only members may join.
All members need to join before message sending starts
Must able to leave at any time(Due to crashes)

Dynamic: May join and leave whenever.
Re-join = New join


###TODO:
- Interface for:
  - Create [ ]
  - Remove [ ]
  - Add member [ ]
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
- Non-reliable multicast [ ]
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
- Unordered [ ]
