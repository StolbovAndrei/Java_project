import com.fasterxml.jackson.annotation.JsonProperty;

    public class GeniusResponse {

        public Response getResponse() {
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }

        @JsonProperty("response")
        private Response response;
    }

