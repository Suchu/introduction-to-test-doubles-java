import com.techreturners.bubbleteaordersystem.model.*;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaMessenger;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaOrderService;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import testhelper.DummySimpleLogger;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BubbleTeaOrderServiceTest {

    private DebitCard testDebitCard;
    private PaymentDetails paymentDetails;
    private DummySimpleLogger dummySimpleLogger;
    private BubbleTeaMessenger mockMessenger;
    private BubbleTeaOrderService bubbleTeaOrderService;

    @Parameterized.Parameter()
    public String name;
    @Parameterized.Parameter(1)
    public String address;
    @Parameterized.Parameter(2)
    public String debitCardInfo;
    @Parameterized.Parameter(3)
    public String bubbleTeaType;

    @Parameterized.Parameters(name = "{index}: name = {0}, address = {1}, debitCardInfo = {2}, bubbleTeaType = {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"hello kitty", "sanrio puroland", "0123456789", "OolongMilkTea"},
                {"hello kitty1", "sanrio puroland1", "0173456786", "PeachIceTea"},
                {"hello kitty2", "sanrio puroland2", "0183456784", "LycheeIceTea"},
                {"hello kitty3", "sanrio puroland3", "0133456781", "JasmineMilkTea"},
                {"hello kitty4", "sanrio puroland4", "0143456785", "MatchaMilkTea"},
        });
    }

    @BeforeEach
    public void setup() {
        DebitCard testDebitCard = new DebitCard(debitCardInfo);
        paymentDetails = new PaymentDetails(name, address, testDebitCard);
        dummySimpleLogger = new DummySimpleLogger();
        mockMessenger = mock(BubbleTeaMessenger.class);
        bubbleTeaOrderService = new BubbleTeaOrderService(dummySimpleLogger, mockMessenger);
    }

    @Test
    public void shouldCreateBubbleTeaOrderRequestWhenCreateOrderRequestIsCalled() {

        //Arrange
        BubbleTea bubbleTea = new BubbleTea(BubbleTeaTypeEnum.OolongMilkTea, 6.78);
        BubbleTeaRequest bubbleTeaRequest = new BubbleTeaRequest(paymentDetails, bubbleTea);

        BubbleTeaOrderRequest expectedResult = new BubbleTeaOrderRequest(
                name,
                address,
                debitCardInfo,
                BubbleTeaTypeEnum.valueOf(bubbleTeaType)
        );
        //        System.out.println(expectedResult.getName());

        //Act
        BubbleTeaOrderRequest result = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequest);
        //        System.out.println(result.getName());

        //Assert
        Assertions.assertEquals(expectedResult.getName(), result.getName());
        Assertions.assertEquals(expectedResult.getAddress(), result.getAddress());
        Assertions.assertEquals(expectedResult.getDebitCardDigits(), result.getDebitCardDigits());
        Assertions.assertEquals(expectedResult.getBubbleTeaType(), result.getBubbleTeaType());

        //Verify Mock was called with the BubbleTeaOrderRequest result object
        verify(mockMessenger).sendBubbleTeaOrderRequestEmail(result);
        verify(mockMessenger, times(1)).sendBubbleTeaOrderRequestEmail(result);
    }

}
