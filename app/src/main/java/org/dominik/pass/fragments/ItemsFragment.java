package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dominik.pass.R;
import org.dominik.pass.http.dto.DataDTO;
import org.dominik.pass.http.enums.DataType;
import org.dominik.pass.models.wrappers.AddressData;
import org.dominik.pass.models.wrappers.AllData;
import org.dominik.pass.models.wrappers.NoteData;
import org.dominik.pass.models.wrappers.PasswordData;
import org.dominik.pass.models.wrappers.SiteData;
import org.dominik.pass.models.entries.AddressEntry;
import org.dominik.pass.models.entries.NoteEntry;
import org.dominik.pass.models.entries.PasswordEntry;
import org.dominik.pass.models.entries.SiteEntry;
import org.dominik.pass.viewmodels.DataViewModel;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ItemsFragment extends Fragment {
  private static final String TAG = "ITEMS_FRAGMENT";
  private DataViewModel dataViewModel;

  private View view;

  private final List<AllData> allData = new LinkedList<>();

  public ItemsFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.fragment_items, container, false);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
    dataViewModel.fetchAllData();
    dataViewModel
      .getAllData()
      .observe(getViewLifecycleOwner(), data -> {
        if (data != null) {
          prepareData(data);
          Log.d(TAG, allData.toString());
        }
      });
  }

  private void prepareData(List<DataDTO> dataDto) {
    ObjectMapper mapper = new ObjectMapper();

    dataDto
      .forEach(data -> {
        try {
         switch (data.getType()) {
           case PASSWORD:
             PasswordData passwordData = createPasswordData(data);
             allData.add(new AllData(passwordData.getPublicId(), passwordData.getPasswordEntry().getEntryTitle(), passwordData.getType()));
             break;
           case ADDRESS:
             AddressData addressData = createAddressData(data);
             allData.add(new AllData(addressData.getPublicId(), addressData.getAddressEntry().getEntryTitle(), addressData.getType()));
             break;
           case SITE:
             SiteData siteData = createSiteData(data);
             allData.add(new AllData(siteData.getPublicId(), siteData.getSiteEntry().getEntryTitle(), siteData.getType()));
             break;
           case NOTE:
             NoteData noteData = createNoteData(data);
             allData.add(new AllData(noteData.getPublicId(), noteData.getNoteEntry().getEntryTitle(), noteData.getType()));
             break;
         }
        } catch (JsonProcessingException ex) {
          ex.printStackTrace();
        }
      });

  }

  private PasswordData createPasswordData(DataDTO data) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    PasswordEntry passwordEntry = mapper.readValue(data.getEntry(), PasswordEntry.class);

    return PasswordData.builder()
      .setPublicId(data.getPublicId())
      .setType(data.getType())
      .setCreatedAt(data.getCreatedAt())
      .setUpdatedAt(data.getUpdatedAt())
      .setPasswordEntry(passwordEntry)
      .build();
  }

  private AddressData createAddressData(DataDTO dataDTO) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    AddressEntry addressEntry = mapper.readValue(dataDTO.getEntry(), AddressEntry.class);

    return AddressData.builder()
      .setPublicId(dataDTO.getPublicId())
      .setType(dataDTO.getType())
      .setCreatedAt(dataDTO.getCreatedAt())
      .setUpdatedAt(dataDTO.getUpdatedAt())
      .setAddressEntry(addressEntry)
      .build();
  }

  private SiteData createSiteData(DataDTO dataDTO) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    SiteEntry siteEntry = mapper.readValue(dataDTO.getEntry(), SiteEntry.class);

    return SiteData.builder()
      .setPublicId(dataDTO.getPublicId())
      .setType(dataDTO.getType())
      .setCreatedAt(dataDTO.getCreatedAt())
      .setUpdatedAt(dataDTO.getUpdatedAt())
      .setSiteEntry(siteEntry)
      .build();
  }

  private NoteData createNoteData(DataDTO dataDTO) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    NoteEntry noteEntry = mapper.readValue(dataDTO.getEntry(), NoteEntry.class);

    return NoteData.builder()
      .setPublicId(dataDTO.getPublicId())
      .setType(dataDTO.getType())
      .setCreatedAt(dataDTO.getCreatedAt())
      .setUpdatedAt(dataDTO.getUpdatedAt())
      .setNoteEntry(noteEntry)
      .build();
  }
}