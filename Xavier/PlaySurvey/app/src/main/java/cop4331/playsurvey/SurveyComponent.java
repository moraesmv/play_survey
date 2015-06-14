package cop4331.playsurvey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.*;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SurveyComponent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SurveyComponent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveyComponent extends Fragment {

    private static LinkedList<SurveyComponent> existing;

    private int[] layouts = {
            R.layout.survey_component,
            R.layout.empty_survey_component,
            R.layout.multiple_choice_component,
            R.layout.true_false_component,
            R.layout.short_answer_component
    };

    private int content = 0;

    public static SurveyComponent newInstance(int position)
    {
        SurveyComponent sc = new SurveyComponent();
        Bundle args = new Bundle();
        args.putInt("position", position);
        sc.setArguments(args);

        return sc;
    }

    public SurveyComponent()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        content = layouts[2]; //getArguments().getInt("position", R.layout.empty_survey_component);

        ViewGroup rootView = (ViewGroup) inflater.inflate(content, container, false);

        return rootView;
    }

}
