package algonquin.cst2335.shu00003;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.shu00003.data.ChatMessage;
import algonquin.cst2335.shu00003.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m){
        selected = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.messageText.setText(selected.getMessage());
        binding.timeText.setText(selected.getTimeSent());
        if (selected.isSentButton()){
            binding.buttonText.setText("Send");
        }
        else {
            binding.buttonText.setText("Receive");
        }

        binding.databaseText.setText("Id= " + selected.id);

        return binding.getRoot();
    }
}
