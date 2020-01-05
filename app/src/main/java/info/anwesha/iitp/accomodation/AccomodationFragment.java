package info.anwesha.iitp.accomodation;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.util.List;

import info.anwesha.iitp.Auth.AuthApi;
import info.anwesha.iitp.R;
import info.anwesha.iitp.network.RetrofitClientInstance;
import info.anwesha.iitp.utils.CheckNetwork;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccomodationFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private ProgressDialog progressDialog;
    private Button submit_button;
    private Spinner accomodation_options_spinner;
    private EditText celestaIdInput, passwordInput;
    private String user_response;
    private SharedPreferences preferences;

    public AccomodationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_accomodation, container, false);
        submit_button = rootview.findViewById(R.id.submit_button);
        accomodation_options_spinner = rootview.findViewById(R.id.accoomo_spinner);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accomodation_options_spinner.setAdapter(adapter);
        if (!user_response.equals("None")) {
            submit_button.setClickable(true);
            submit_button.setEnabled(true);
            submit_button.setOnClickListener(v -> {
                if (!CheckNetwork.isNetworkConnected(requireContext())) {
                    Toast.makeText(requireContext(), "You are not connected to the Internet!", Toast.LENGTH_LONG).show();
                } else submit();
            });
        } else {
            submit_button.setEnabled(false);
            submit_button.setClickable(false);
        }
    }

    private void submit() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Finding a spot for you...");
        progressDialog.show();
        String accessToken = preferences.getString("access_token", "");
        info.anwesha.iitp.Auth.AuthApi authApi = RetrofitClientInstance.getRetrofitInstance().create(AuthApi.class);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("anweshaId", celestaIdInput.getText().toString().trim())
                .addFormDataPart("access_token", accessToken)
                .addFormDataPart("day1", user_response)
                .addFormDataPart("day2", user_response)
                .addFormDataPart("day3", user_response)
                .addFormDataPart("day1_day2", user_response)
                .addFormDataPart("day2_day3", user_response)
                .addFormDataPart("all_day", user_response)
                .build();

        Call<info.anwesha.iitp.accomodation.AccommodationResponse> call = authApi.accommodationResponse(requestBody);
        call.enqueue(new Callback<AccommodationResponse>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationResponse> call, @NonNull Response<AccommodationResponse> response) {
                if (progressDialog != null) progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    info.anwesha.iitp.accomodation.AccommodationResponse accommodationResponse = response.body();

                    if (accommodationResponse.getAccommoResponse() == 202) {
                        Log.e("Success", "access_token: " + accommodationResponse.getAccess_token());
                        storeData(accommodationResponse);
                        Toast.makeText(requireContext(), "Accommodation Booking Successful!!!", Toast.LENGTH_LONG).show();
                        if (getActivity() != null)
                            getActivity().finish();
                    } else if (accommodationResponse.getAccommoResponse() == 401) {
                        List<String> message = AccommodationResponse.getMessage();  //look, HERE IS STATIC FIELD!!!
                        Toast.makeText(requireContext(), message.get(0), Toast.LENGTH_LONG).show();
                    } else if (accommodationResponse.getAccommoResponse() == 208) {
                        Toast.makeText(requireContext(), "This Anwesha-id has already booked Accommodation", Toast.LENGTH_LONG).show();
                    } else if (accommodationResponse.getAccommoResponse() == 404) {
                        Toast.makeText(requireContext(), "The entered Anwesha id does not exist!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccommodationResponse> call, @NonNull Throwable t) {
                if (progressDialog != null) progressDialog.dismiss();
                Log.e("Error", "onFailure: " + t.getMessage());
                Toast.makeText(requireContext(), "Something went wrong!!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void storeData(AccommodationResponse accommodationResponse) {
        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
                .putString("anwesha_id", accommodationResponse.getAnweshaId())
                .putString("day!", accommodationResponse.getDay1())
                .putString("access_token", accommodationResponse.getAccess_token())
                .putString("message", String.valueOf(accommodationResponse.getMessage()))
                .apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        accomodation_options_spinner.setOnItemSelectedListener(this);
        parent.getItemAtPosition(position);
        user_response = accomodation_options_spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
