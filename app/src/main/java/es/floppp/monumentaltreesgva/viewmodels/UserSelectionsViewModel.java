package es.floppp.monumentaltreesgva.viewmodels;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.floppp.monumentaltreesgva.extras.K;
import es.floppp.monumentaltreesgva.extras.SingleLiveEvent;
import es.floppp.monumentaltreesgva.pojos.Tree;

public class UserSelectionsViewModel extends ViewModel {
    private MutableLiveData<K.Region> message = new MutableLiveData<>();
    private SingleLiveEvent<Tree> treeMessage =  new SingleLiveEvent<>();

    public LiveData<K.Region> regionChanged() {
        return this.message;
    }

    // Podríamos usar AndroidViewModel y a partir de application coger recursos
    // y obtener el string que elegimos, pero nos puede dar problemas (supongo,
    // no he comprobado) por internacionalización, así que mejor ir a lo seguro
    // y usar el índice del spinner.
    public void postRegion(int n) {
        K.Region selectedRegion = null;
        switch (n) {
            case 0:
                selectedRegion = K.Region.VALENCIA;
                break;
            case 1:
                selectedRegion = K.Region.ALICANTE;
                break;
            case 2:
                selectedRegion = K.Region.CASTELLON;
                break;
            default:
                break;
        }

        this.message.setValue(selectedRegion);
    }

    public void postTree(Tree tree) {
        this.treeMessage.setValue(tree);
    }

    public LiveData<Tree> treeSelected() {
        return this.treeMessage;
    }
}
