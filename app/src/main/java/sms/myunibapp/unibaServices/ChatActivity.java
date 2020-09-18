package sms.myunibapp.unibaServices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myunibapp.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import sms.myunibapp.oggetti.DrawerActivity;

public class ChatActivity extends DrawerActivity {


    private FirebaseListAdapter<ChatMessage> adapter;

    private ListView messaggi;
    private EditText input;
    private FloatingActionButton invio;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messaggi=findViewById(R.id.lista_messaggi);
        input=findViewById(R.id.chat_input);
        invio=findViewById(R.id.fab);

        user= DrawerActivity.sessionManager.getSessionUsername();


        DatabaseReference dr=FirebaseDatabase.getInstance().getReference("Conversazioni");
        FirebaseListAdapter<ChatMessage> adapter=new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.template_chat, dr) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView mess=v.findViewById(R.id.message_text);
                TextView utente=v.findViewById(R.id.message_user);
                TextView data=v.findViewById(R.id.message_time);

                mess.setText(model.getMessageText());
                utente.setText(model.getMessageUser());
                data.setText(DateFormat.format("dd/MM/yyyy (HH:mm)", model.getMessageTime()));
            }
        };
        messaggi.setAdapter(adapter);

        invio.setOnClickListener((View v) -> {
            String in=input.getText().toString().trim();
            if(!in.isEmpty()){//se la stringa non è vuota
                ChatMessage message = new ChatMessage(in, user);
                dr.push().setValue(message);
                input.setText("");
            }
        });
    }
}

class ChatMessage{
    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){}

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}