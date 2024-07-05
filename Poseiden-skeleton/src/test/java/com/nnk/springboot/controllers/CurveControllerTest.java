package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.ICrudService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CurveController.class)
@WithMockUser(roles = "USER")
public class CurveControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ICrudService<CurvePoint> curvePointService;
    @InjectMocks
    private CurveController controller;

    @Test
    public void homeTest() throws Exception {
        when(curvePointService.getAll()).thenReturn(List.of(new CurvePoint((byte) 1, 20d, 20d)));
        mockMvc.perform(get("/curvePoint/list")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", hasSize(1)));

        verify(curvePointService, times(1)).getAll();
    }

    @Test
    public void addCurvePointForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @DisplayName("Given fields are valid, then save curvepoint to database and return add view")
    public void validateAddCurvePointTest() throws Exception {
        when(curvePointService.save(ArgumentMatchers.any(CurvePoint.class))).thenReturn(new CurvePoint());

        mockMvc.perform(post("/curvePoint/validate")
                        .param("CurveId", "10")
                        .param("term", "10")
                        .param("value", "100")
                        .with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));


        verify(curvePointService, times(1)).save(ArgumentMatchers.any(CurvePoint.class));
    }

    @Test
    @DisplayName("Given curveid field is not valid, then don't save curvepoint and return add view with errors")
    public void whenCurveIdFieldHasErrors_thenDontSaveCurvePoint() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("CurveId", "")
                        .param("term", "10")
                        .param("value", "100")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "curveId"));

        verify(curvePointService, never()).save(ArgumentMatchers.any(CurvePoint.class));
    }

    @Test
    @DisplayName("Given there is a curve point with the id, then show in template")
    public void showUpdateFormTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint((byte) 10, 15d, 12d);
        when(curvePointService.getById(1)).thenReturn(Optional.of(curvePoint));

        mockMvc.perform(get("/curvePoint/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attribute("curvePoint", hasProperty("curveId", is((byte) 10))))
                .andExpect(model().attribute("curvePoint", hasProperty("term", is(15.0))))
                .andExpect(model().attribute("curvePoint", hasProperty("value", is(12.0))));
    }

    @Test
    @DisplayName("Given fields have no errors, then update curve point and return list view")
    public void updateCurvePoint() throws Exception {
        mockMvc.perform(post("/curvePoint/update/{id}", 1)
                        .param("curveId", "10")
                        .param("value", "11")
                        .param("term", "12")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).update(eq(1), ArgumentMatchers.any(CurvePoint.class));
    }

    @Test
    @DisplayName("Given fields have errors, then don't update curve point and return view with errors")
    public void whenNoCurvePointWithId_thenDontSave() throws Exception {
        mockMvc.perform(post("/curvePoint/update/{id}", 1)
                        .param("curveId", "")
                        .param("value", "11")
                        .param("term", "12")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/curvePoint/update"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "curveId"));

        verify(curvePointService, never()).update(eq(1), ArgumentMatchers.any(CurvePoint.class));
    }

    @Test
    @DisplayName("Given curve point with id exists, then delete and redirect to list")
    public void deleteCurvePointSuccessTest() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).delete(1);
    }

    @Test
    @DisplayName("Given curve point id doesn't exists, then don't delete and redirect to list")
    public void deleteCurvePointFailsTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(curvePointService).delete(1);

        mockMvc.perform(get("/curvePoint/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }
}
