import org.dreambot.api.script.AbstractScript;

public class BlastFurnace extends AbstractScript{
    private enum ScriptState {IDLE, RUNNING, COOKS_ASSISTANT, MINING, KNIGHTS_SWORD, ROMEO_AND_JULIET, SMELTING};
    private ScriptState state;

    @Override
    public void onStart(){
        state = ScriptState.IDLE;
    }

    @Override
    public int onLoop(){

        switch(state){
            case IDLE:
                break;
            case RUNNING:
                break;
            case COOKS_ASSISTANT:
                break;
            case MINING:
                break;
            case KNIGHTS_SWORD:
                break;
            case ROMEO_AND_JULIET:
                break;
            case SMELTING:
                break;
            default:
                return 100;
        }

        return 100;
    }
}