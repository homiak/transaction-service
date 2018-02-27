package form3.challenge.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RequiredArgsConstructor
class MockHvcHelper {

    private final MockMvc mockMvc;

    public ResultActions async(final MockHttpServletRequestBuilder builder, final String payload, final MediaType mediaType) throws Exception {
        builder.content(payload).contentType(mediaType);
        MvcResult resultActions = this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();
        return this.mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(resultActions));
    }

    public ResultActions syncPost(final String url, final String payload) throws Exception {
        return sync(MockMvcRequestBuilders.post(url, new Object[0]), payload);
    }

    public ResultActions syncPut(final String url, final String payload) throws Exception {
        return sync(MockMvcRequestBuilders.put(url, new Object[0]), payload);
    }

    public ResultActions sync(final MockHttpServletRequestBuilder builder, final String payload) throws Exception {
        builder.content(payload).contentType(MediaType.APPLICATION_JSON);
        return this.mockMvc.perform(builder);
    }

    public ResultActions sync(final MockHttpServletRequestBuilder builder) throws Exception {
        builder.contentType(MediaType.APPLICATION_JSON);
        return this.mockMvc.perform(builder);
    }
}
