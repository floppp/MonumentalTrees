package es.floppp.monumentaltreesgva.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.floppp.monumentaltreesgva.extras.K;

public class RegionViewModel extends ViewModel {
    private MutableLiveData<K.Region> message = new MutableLiveData<>();

    public LiveData<K.Region> regionChanged() {
        return this.message;
    }

    // Podríamos usar AndroidViewModel y a partir de application coger recursos
    // y obtener el string que elegimos, pero nos puede dar problemas (supongo,
    // no he comprobado) por internacionalización, así que mejor ir a lo seguro
    // y usar el índice del spinner.
    public void post(int n) {
        K.Region selectedRegion = K.Region.VALENCIA;
        switch (n) {
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
}
