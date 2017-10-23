# LowbrainBinding
Bind custom commands to quick inventory menu

## Configuration
``` yaml
## set the maximum bindings possible between 1 and 36
maximum_bindings: 18
```

## Permissions
``` yaml
permissions:
    lb.binding.use:
        description: Lets player use all binding commands
        default: op
    lb.binding.save:
        description: allow to save manually
        default: op
```

## Commands
- /lb bind <slot> <command> [<args...>] ==> bind command to a specific slot
- /lb unbind <slot> ==> unbind specific slot
- /lb unbindall ==> unbind all slot/binds
  
Where <slot> is the slut number you wish to bind to (from 0 to 35)
Where <command> is the command you wish to execute
Where [<args...>] is all additional arguments used with the command
  
e.g : /lb bind 0 say hello world

## Usage

Each player can have up to 36 binds (maximum_bindings). Bind are saved in data.yml using the player's online GUID

To open the bind menu, player must first open their inventory and right clicking anywhere outside the current inventory. Then, the binds menu will open, listing all the player's binds.

*** need screetshot here ***
