package com.smita.dairy.common.security;

import com.smita.dairy.farmer.FarmerController;
import com.smita.dairy.farmer.FarmerService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FarmerController.class)
@Import(SecurityConfig.class)
@TestPropertySource(
        properties = {
                "app.jwt.secret=test-jwt-secret-for-security-tests-1234567890",
                "app.jwt.expiration-minutes=120"
        }
)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FarmerService farmerService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldRejectProtectedEndpointWithoutJwt()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/farmers")
        )
        .andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    void shouldAllowAdminToAccessProtectedEndpoint()
            throws Exception {

        when(farmerService.getAllFarmers())
                .thenReturn(List.of());

        mockMvc.perform(
                get("/api/v1/farmers")
                        .with(
                                jwt().authorities(
                                        new SimpleGrantedAuthority(
                                                "ROLE_ADMIN"
                                        )
                                )
                        )
        )
        .andExpect(
                status().isOk()
        );
    }

    @Test
    void shouldAllowOperatorToAccessNormalProtectedEndpoint()
            throws Exception {

        when(farmerService.getAllFarmers())
                .thenReturn(List.of());

        mockMvc.perform(
                get("/api/v1/farmers")
                        .with(
                                jwt().authorities(
                                        new SimpleGrantedAuthority(
                                                "ROLE_OPERATOR"
                                        )
                                )
                        )
        )
        .andExpect(
                status().isOk()
        );
    }
}