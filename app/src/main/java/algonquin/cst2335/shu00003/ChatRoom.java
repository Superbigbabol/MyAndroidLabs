package algonquin.cst2335.shu00003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.shu00003.data.ChatMessage;
import algonquin.cst2335.shu00003.data.ChatMessageDAO;
import algonquin.cst2335.shu00003.data.ChatRoomViewModel;
import algonquin.cst2335.shu00003.data.MessageDatabase;
import algonquin.cst2335.shu00003.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.shu00003.databinding.ReceiveMessageBinding;
import algonquin.cst2335.shu00003.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages;
    ChatMessageDAO mDAO;

    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener( click -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);//post in the ViewModel MutableLiveData

//                int position = getAbsoluteAdapterPosition();//return which row was clicked
//
//                ChatMessage clickedMessage = messages.get(position);//get ChatMessage at the position
//                // create alertdialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
//                builder.setMessage("Do you want to delete this message?"+messageText.getText())
//                        .setTitle("Question:")
//                        .setPositiveButton("Yes", (dialogInterface, i) -> {
//                    Executor thread = Executors.newSingleThreadExecutor();
//                    thread.execute(() ->
//                    {
//                        //run in background thread
//                        mDAO.deleteMessage(clickedMessage);//delete from db
//                        messages.remove(position);//remove from ArrayList
//                        //go back to main thread
//                        runOnUiThread(()->{
//                            myAdapter.notifyItemRemoved(position);//update the recycler view
//                            Snackbar.make(messageText, "You deleted message #"+position, Snackbar.LENGTH_LONG)
//                                    .setAction("Undo", clk -> {
//                                        Executor thread_2 = Executors.newSingleThreadExecutor();
//                                        thread_2.execute(() ->{
//                                            //background
//                                            mDAO.insertMessage(clickedMessage);
//                                            messages.add(position,clickedMessage);
//                                            //main thread
//                                            runOnUiThread(()->{
//                                                myAdapter.notifyItemInserted(position);
//                                            });
//                                        });
//                                    })
//                                    .show();
//                        });
//
//                    });
//                })
//                .setNegativeButton("No",(dialogInterface, i) -> {
//                    //do nothing
//                })
//                .create().show();

            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_1:
                // get selected message from ChatRoomViewModel
                ChatMessage sm = chatModel.selectedMessage.getValue();
                // get position in arraylist
                int position = messages.indexOf(sm);
                // create alertdialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete this message, "+sm.getMessage()+"?")
                        .setTitle("Question:")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->
                    {
                        //run in background thread
                        mDAO.deleteMessage(sm);//delete from db
                        messages.remove(position);//remove from ArrayList
                        //go back to main thread
                        runOnUiThread(()->{
                            myAdapter.notifyItemRemoved(position);//update the recycler view
                            Snackbar.make(binding.fragmentLocation, "You deleted message #"+position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        Executor thread_2 = Executors.newSingleThreadExecutor();
                                        thread_2.execute(() ->{
                                            //background
                                            mDAO.insertMessage(sm);
                                            messages.add(position,sm);
                                            //main thread
                                            runOnUiThread(()->{
                                                myAdapter.notifyItemInserted(position);
                                            });
                                        });
                                    })
                                    .show();
                        });

                    });
                })
                .setNegativeButton("No",(dialogInterface, i) -> {
                    //do nothing
                })
                .create().show();
                break;
            case R.id.item_2:
                Toast.makeText(ChatRoom.this, "Version 1.0, created by Bo Shu", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.myToolbar);

        //prepare recycler view adapter
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType==0)
                {
                    SentMessageBinding sbinding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(sbinding.getRoot());
                }
                ReceiveMessageBinding rbinding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(rbinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                if (messages.get(position).isSentButton())
                {
                    return 0;
                }
                return 1;
            }
        };

        //create a database "ChatHistory" and apply DAO
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),MessageDatabase.class,"ChatHistory").build();
        mDAO = db.cmDAO();

        //instantiate a ViewModel so that chat messages can survive from screen rotation
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null)
        {
            messages = new ArrayList<>();

            //get chat history from db in another thread
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                //on a second thread
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                // on main thread
                //runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter ));
            });
            chatModel.messages.postValue(messages);
        }
        //load the RecyclerView
        binding.recycleView.setAdapter( myAdapter );
        //display recycle view
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));


        binding.sendButton.setOnClickListener( click -> {
            String input = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage cm = new ChatMessage(input, currentDateandTime, true);
            messages.add(cm);//append to ArrayList

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->
            {
                long id = mDAO.insertMessage(cm);//insert to db and return a long id
                cm.id = id;//database is saying what the id is and assign to the ChatMessage obj
            });

            myAdapter.notifyItemInserted(messages.size()-1);//notify recycler view that a MyRowHolder was inserted
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener( click -> {
            String receiveMsg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage cm = new ChatMessage(receiveMsg, currentDateandTime, false);
            messages.add(cm);

            Executor thread_1 = Executors.newSingleThreadExecutor();
            thread_1.execute(() ->
            {
                long id = mDAO.insertMessage(cm);
                cm.id = id;
            });

            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });

        chatModel.selectedMessage.observe(this, newMessageValue ->{
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
//            FragmentManager fMgr = getSupportFragmentManager();
//            FragmentTransaction tx = fMgr.beginTransaction();
//            tx.add(R.id.fragmentLocation, chatFragment);
//            tx.commit();

            //builder pattern
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, chatFragment)
                    .addToBackStack("")
                    .commit();
        });

    }
}