package com.example.mos3if;

import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FirstAidFragment extends Fragment {

    RecyclerView RecyclerViewer;
    ArrayList<Model> arrayList = new ArrayList<>();
    public FirstAidFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_first_aid, container, false);
        RecyclerViewer= v.findViewById(R.id.RecyclerViewer);
        RecyclerViewer.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList.add(new Model(R.drawable.cpr, "CPR TREATMENT",
                "If the person does not respond and is not breathing or only gasping, CALL 9-1-1 and get equipment, or tell someone to do so",
                "Kneel beside the person. Place the person on their back on a firm, flat surface",
                "Give 30 chest compressions\n" +
                        "\n" +
                        "* Hand position: Two hands centered on the chest\n" +
                        "* Body position: Shoulders directly over hands; elbows locked\n" +
                        "* Depth: At least 2 inches\n" +
                        "* Rate: 100 to 120 per minute\n" +
                        "* Allow chest to return to normal position after each compression",
                "Give 2 breaths\n" +
                        "\n" +
                        "* Open the airway to a past-neutral position using the head-tilt/chin-lift technique\n" +
                        "* Pinch the nose shut, take a normal breath, and make complete seal over the person’s mouth with your mouth.\n" +
                        "* Ensure each breath lasts about 1 second and makes the chest rise; allow air to exit before giving the next breath\n" +
                        "Note: If the 1st breath does not cause the chest to rise, retilt the head and ensure a proper seal before giving the 2nd breath If the 2nd breath does not make the chest rise, an object may be blocking the airway",
                "Continue giving sets of 30 chest compressions and 2 breaths. Use an AED as soon as one is available! Minimize interruptions to chest compressions to less than 10 seconds."));


        arrayList.add(new Model(R.drawable.heartattack, "HEART ATTACK",
                "Call your local emergency number for emergency help straight away and tell them you think someone is having a heart attack.",
                "Help move the casualty into a comfortable position. The best position is on the floor, with their knees bent and their head and shoulders supported.",
                "Give them one aspirin tablet (300mg) and ask them to chew it slowly(Do not give aspirin to the casualty if they are under 16 or if they are allergic to it",
                "Ask the casualty to take their own angina medication, if they have some",
                "Keep monitoring the casualty’s level of response until emergency help arrives.If they become unresponsive at any point, prepare to start CPR."));

        arrayList.add(new Model(R.drawable.burn, "BURNS",
                "First make sure that the area around the casualty is safe. Wear protective gloves, this will prevent you from coming into contact with the chemical",
                "Flood the burn with cool or lukewarm running water until the ambulance arrives, to disperse the chemical and stop it burning",
                "If the burn is severe, call 999 or 112 for emergency help. Pass on any details you may have of the chemical to ambulance control",
                "Carefully remove any contaminated clothing.",
                "If the burn is minor and you have not yet called for help, send the casualty to hospital. Monitor their level of response while waiting for help"));

        arrayList.add(new Model(R.drawable.bleeding, "BLEEDING",
                "Call your local emergency number if the wound is deep or you're not sure how serious it is",
                "Remove any clothing or debris from the wound. Look for the source of the bleeding. There could be more than one injury. Remove any obvious debris but don't try to clean the wound",
                "Stop the bleeding by covering the wound with sterile gauze or a clean cloth. Press on it firmly with the palm of your hand until bleeding stop then help the injured person lie down",
                "Keep the person still and try to keep the injured person from moving till the help arrives",
                "After helping the injured person, wash your hands, even if it doesn't look like any blood got on your hands."));

//        arrayList.add(new Model(R.drawable.anaphylaxis, "ANAPHYLAXIS",));
        arrayList.add(new Model(R.drawable.drown, "DROWNING",
                "Check for breathing. Tilt their head back and look, listen and feel for breaths. If they are not breathing, move on to the following steps",
                "Give five rescue breaths: tilt their head back, sealing your mouth over their mouth. Pinch their nose and blow into their mouth. Repeat this five times",
                "Give 30 chest compressions. Push firmly in the middle of their chest and then release. Repeat this 30 times",
                "Give two rescue breaths then continue with cycles of 30 chest compressions and two rescue breaths until help arrives or the casualty shows signs of becoming responsive",
                "If the casualty starts to breathe normally, keep them still and treat for hypothermia by keeping them warm and dry if possible"));

        arrayList.add(new Model(R.drawable.ellectricshpck, "ELECTRIC SHOCK",
                "Do not touch the casualty if they’re still in contact with the electrical source as you are at risk of electrocution",
                "Turn off the source of electricity to break the contact between the electrical supply and the casualty",
                "Alternatively, move the casualty away from the source. You may be able to stand on some dry insulating material (such as a plastic mat or wooden box) and use a broom handle or wooden pole to push the casualty's limb away from the source",
                "If it’s not possible to break contact using a wooden object, loop some rope around the underneath of the casualty’s arms or ankles and pull them away from the electrical source",
                "Once you’re sure the contact has been broken between the casualty and the electrical source, perform a primary survey and treat any injuries and call your local emergency number"));

        arrayList.add(new Model(R.drawable.fracture, "FRACTURES",
                "If it is an open fracture, cover the wound with a sterile dressing or a clean non-fluffy cloth. Apply pressure around the wound and not over the protruding bone, to control any bleeding. Then secure the dressing with a bandage",
                "Advise the casualty to keep still while you support the injured part to stop it from moving. Do this by holding the joint above and below the injured area",
                "Place padding around the injury for extra support",
                "Once you’ve done this, call 999 or 112 for emergency help. Do not move the casualty until the injured part is secured, unless they are in immediate danger. You can secure an upper limb fracture with a sling and a lower limb fracture with broad fold bandages",
                "If necessary treat for shock, but do not raise the legs if either are suspected to be broken or there is injury to the pelvis or a hip. Monitior until help arrives"));

        arrayList.add(new Model(R.drawable.poisonous, "POISONING",
                "If you think someone has food poisoning, advise them to lie down and rest. If they’re vomiting, give them small sips of water to drink as this will help prevent dehydration. If they have accompanying diarrhoea or diarrhoea only, it is even more important to try to replace lost fluids and salts",
                "When they feel hungry again, advise them to eat light, bland, easily digested foods, such as bread, rice, crackers, or a banana",
                "If they get worse and the vomiting and diarrhoea is persistent, particularly in the elderly, babies, or young children, seek medical advice. Call 999 or 112 for emergency help",
                "To prevent the spread of the infection, always use and encourage good hand hygiene",
                "Stay off work or school until at least 48 hours after the last episode of diarrhoea or vomiting"));
////        arrayList.add(new Model(R.drawable.stroke, "STROKE",));

        ModelRecyclerViewer modelRecyclerViewer = new ModelRecyclerViewer(getContext(),arrayList);
        RecyclerViewer.setAdapter(modelRecyclerViewer);
        RecyclerViewer.setLayoutManager(new GridLayoutManager(getContext(),2));
        RecyclerViewer.setAdapter(modelRecyclerViewer);



        return v;
    }
}