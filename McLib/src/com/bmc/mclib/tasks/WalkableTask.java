package com.bmc.mclib.tasks;

import com.bmc.mclib.interfaces.Walkable;
import com.bmc.mclib.script.McScript;

public abstract class WalkableTask extends SingleExecutionTask implements Walkable {
    public WalkableTask(McScript s) {
        super(s);

//        s.log(new Integer(s.getTasks()).toString());
//        s.log(s.getTasks());

        s.getTasks().addTask(s.getTasks().indexOf(this), new WalkImpl(s));
    }

    class WalkImpl extends Task {
        public WalkImpl(McScript s) {
            super(s);
        }

        @Override
        public boolean validate() {
            return false;
//            return canWalk();
        }

        @Override
        public int execute() {
            return 0;
//            return doWalk();
        }
    }
}
